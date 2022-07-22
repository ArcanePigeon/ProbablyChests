package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.utils.PCLockedState;

import java.util.Optional;

public class UndergroundChestFeature extends Feature<DefaultFeatureConfig> {
	public UndergroundChestFeature (Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<DefaultFeatureConfig> context) {
		net.minecraft.util.math.random.Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin().up();
		DefaultFeatureConfig config = context.getConfig();
		BlockState blockToBePlaced = null;
		boolean isWater = structureWorldAccess.getBlockState(pos).isOf(Blocks.WATER);
		boolean isNether = structureWorldAccess.getDimension().ultrawarm();
		boolean hasGoldLock = false;
		PCLockedState lockedState = PCLockedState.UNLOCKED;
		if (isWater) {
			if (random.nextFloat() < 0.85F) {
				return false;
			}
			if(structureWorldAccess.getBlockState(pos.up()).isSolidBlock(structureWorldAccess,pos.up())){
				return false;
			}
			if (structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.ICY) || structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.SNOWY)) {
				blockToBePlaced = PCBlocks.ICE_CHEST.getDefaultState();
			} else {
				blockToBePlaced = PCBlocks.CORAL_CHEST.getDefaultState();
			}
		} else {
			Optional<CaveSurface> optional = CaveSurface.create(structureWorldAccess, pos, 64, UndergroundChestFeature::canGenerate, UndergroundChestFeature::canReplace);
			if (! optional.isPresent() || ! (optional.get() instanceof CaveSurface.Bounded)) {
				return false;
			}
			CaveSurface.Bounded bounded = (CaveSurface.Bounded) optional.get();
			if (bounded.getHeight() < 3) {
				return false;
			}
			if (bounded.getFloor() > pos.getY()) {
				return false;
			}
			if(isNether){
				blockToBePlaced = PCBlocks.NETHER_CHEST.getDefaultState();
			}
			BlockPos biomeCheckPos = pos.offset(Direction.UP, 5);
			// Set block type based on environment
			if (random.nextFloat() < 0.85F) {
				if (structureWorldAccess.getBiome(biomeCheckPos).matchesKey(BiomeKeys.LUSH_CAVES)) {
					blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState();
				} else if (structureWorldAccess.getBiome(biomeCheckPos).matchesKey(BiomeKeys.DRIPSTONE_CAVES)) {
					blockToBePlaced = PCBlocks.ROCKY_CHEST.getDefaultState();
				}
			}
			if (blockToBePlaced == null) {
				if (pos.getY() <= 0) {
					if (random.nextFloat() < 0.25F) {
						blockToBePlaced = PCBlocks.GOLD_CHEST.getDefaultState();
						hasGoldLock = true;
					} else {
						blockToBePlaced = PCBlocks.STONE_CHEST.getDefaultState();
					}
				} else {
					if (structureWorldAccess.getBiome(biomeCheckPos).isIn(ConventionalBiomeTags.SNOWY) || structureWorldAccess.getBiome(biomeCheckPos).isIn(ConventionalBiomeTags.ICY)) {
						blockToBePlaced = PCBlocks.ICE_CHEST.getDefaultState();
					} else {
						if (random.nextFloat() < 0.25F) {
							blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState();
						} else {
							blockToBePlaced = PCBlocks.NORMAL_CHEST.getDefaultState();
						}
					}
				}
			}
		}
		if(hasGoldLock){
			lockedState = PCLockedState.LOCKED;
		}
		//structureWorldAccess.setBlockState(pos, blockToBePlaced.with(PCProperties.PC_LOCKED_STATE, lockedState), 3);
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BlockPos debugPos = pos;
			for (int i = 0; i < 40; i++) {
				structureWorldAccess.setBlockState(debugPos = debugPos.up(), Blocks.END_ROD.getDefaultState(), 3);
			}
		}
		structureWorldAccess.setBlockState(pos, blockToBePlaced.with(Properties.WATERLOGGED, isWater).with(PCProperties.PC_LOCKED_STATE,lockedState), 3);
		PCBaseChestBlockEntity chest = (PCBaseChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
			chest.hasGoldLock = hasGoldLock;
			chest.isLocked = hasGoldLock;
		}
		return true;
	}

	public static boolean canReplace (BlockState state) {
		return state.isIn(BlockTags.BASE_STONE_OVERWORLD) ||
				state.isIn(BlockTags.DEEPSLATE_ORE_REPLACEABLES) ||
				state.isIn(BlockTags.REPLACEABLE_PLANTS) ||
				state.isIn(BlockTags.LUSH_GROUND_REPLACEABLE) ||
				state.isIn(BlockTags.BASE_STONE_NETHER);
	}

	public static boolean canGenerate (BlockState state) {
		return state.isAir() || state.isOf(Blocks.WATER);
	}
}
