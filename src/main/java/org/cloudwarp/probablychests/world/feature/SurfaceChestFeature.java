package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.utils.PCLockedState;

public class SurfaceChestFeature extends Feature<DefaultFeatureConfig> {
	public SurfaceChestFeature (Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<DefaultFeatureConfig> context) {
		net.minecraft.util.math.random.Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		DefaultFeatureConfig config = context.getConfig();
		BlockState blockToBePlaced = null;
		// Set block type based on environment
		boolean hasVoidLock = false;
		PCLockedState lockedState = PCLockedState.UNLOCKED;
		if (! structureWorldAccess.getBlockState(pos.down()).isSolidBlock(structureWorldAccess, pos)) {
			return false;
		}
		boolean isEnd = structureWorldAccess.getBiome(pos).isIn(BiomeTags.IS_END);
		Biome biome = structureWorldAccess.getBiome(pos).value();

		if (isEnd) {
			hasVoidLock = true;
			blockToBePlaced = PCBlocks.SHADOW_CHEST.getDefaultState();
		} else if (structureWorldAccess.getBiome(pos).isIn(BiomeTags.IS_OCEAN)) {
			if(structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.ICY)){
				blockToBePlaced = PCBlocks.ICE_CHEST.getDefaultState();
			}else{
				return false;
			}
		} else {
			if (structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.FLOWER_FORESTS) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE)) {
				blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState();
			} else if (isBiomeWithinTempRange(biome, 1F, 10.0F) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_HOT)) {
				blockToBePlaced = PCBlocks.ROCKY_CHEST.getDefaultState();
			} else if(structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.SNOWY)){
				blockToBePlaced = PCBlocks.ICE_CHEST.getDefaultState();
			} else if(structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.BEACH)){
				blockToBePlaced = PCBlocks.CORAL_CHEST.getDefaultState();
			}else{
				blockToBePlaced = PCBlocks.NORMAL_CHEST.getDefaultState();
			}
		}

		if(hasVoidLock){
			lockedState = PCLockedState.LOCKED;
		}
		structureWorldAccess.setBlockState(pos, blockToBePlaced.with(PCProperties.PC_LOCKED_STATE, lockedState), 3);
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BlockPos debugPos = pos;
			for (int i = 0; i < 60; i++) {
				structureWorldAccess.setBlockState(debugPos = debugPos.up(), Blocks.END_ROD.getDefaultState(), 3);
			}
		}
		PCBaseChestBlockEntity chest = (PCBaseChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
			chest.hasVoidLock = hasVoidLock;
			chest.isLocked = hasVoidLock;
		}
		return true;
	}

	public static boolean isBiomeWithinTempRange (Biome biome, float minTemp, float maxTemp) {
		return biome.getTemperature() >= minTemp && biome.getTemperature() < maxTemp;
	}
}
