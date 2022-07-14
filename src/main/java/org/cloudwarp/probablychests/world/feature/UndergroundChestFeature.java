package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;

import java.util.Optional;
import java.util.Random;

public class UndergroundChestFeature extends Feature<DefaultFeatureConfig> {
	public UndergroundChestFeature (Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<DefaultFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin().up();
		DefaultFeatureConfig config = context.getConfig();
		BlockState blockToBePlaced = null;
		Optional<CaveSurface> optional = CaveSurface.create(structureWorldAccess, pos, 64, UndergroundChestFeature::canGenerate, UndergroundChestFeature::canReplace);
		if (!optional.isPresent() || !(optional.get() instanceof CaveSurface.Bounded)) {
			return false;
		}
		CaveSurface.Bounded bounded = (CaveSurface.Bounded)optional.get();
		if (bounded.getHeight() < 3) {
			return false;
		}
		if(bounded.getFloor() > pos.getY()){
			return false;
		}
		BlockPos biomeCheckPos = pos.offset(Direction.UP,5);
		if (structureWorldAccess.getDimension().isUltrawarm()) {
			blockToBePlaced = PCBlocks.STONE_CHEST.getDefaultState();
		}
		// Set block type based on environment
		if(random.nextFloat() < 0.85F) {
			if (structureWorldAccess.getBiome(biomeCheckPos).matchesKey(BiomeKeys.LUSH_CAVES)) {
				blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState(); // LUSH CHEST
			} else if (structureWorldAccess.getBiome(biomeCheckPos).matchesKey(BiomeKeys.DRIPSTONE_CAVES)) {
				blockToBePlaced = PCBlocks.ROCKY_CHEST.getDefaultState(); // SANDSTONE CHEST
			}
		}
		if(blockToBePlaced == null) {
			if (pos.getY() <= 0) {
				if (random.nextFloat() < 0.25F) {
					blockToBePlaced = PCBlocks.GOLD_CHEST.getDefaultState(); // GOLD CHEST
				} else {
					blockToBePlaced = PCBlocks.STONE_CHEST.getDefaultState(); // DEEP SLATE CHEST
				}
			} else {
				if (random.nextFloat() < 0.25F) {
					blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState(); // LUSH CHEST
				} else {
					blockToBePlaced = PCBlocks.NORMAL_CHEST.getDefaultState(); // NORMAL CHEST
				}
			}
		}
		if(pos.getY() > bounded.getFloor() + 1){
		pos = pos.offset(Direction.DOWN,pos.getY() - (bounded.getFloor() + 1));
		}

		structureWorldAccess.setBlockState(pos, blockToBePlaced, 3);
		//structureWorldAccess.setBlockState(pos.up(), Blocks.SOUL_CAMPFIRE.getDefaultState(), 3);
		PCChestBlockEntity chest = (PCChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
		}
		return true;
	}

	public static boolean canReplace(BlockState state) {
		return state.isIn(BlockTags.BASE_STONE_OVERWORLD) ||
				state.isIn(BlockTags.DEEPSLATE_ORE_REPLACEABLES) ||
				state.isIn(BlockTags.REPLACEABLE_PLANTS) ||
				state.isIn(BlockTags.LUSH_GROUND_REPLACEABLE) ||
				state.isIn(BlockTags.BASE_STONE_NETHER);
	}

	public static boolean canGenerate(BlockState state) {
		return state.isAir() || state.isOf(Blocks.WATER);
	}
}
