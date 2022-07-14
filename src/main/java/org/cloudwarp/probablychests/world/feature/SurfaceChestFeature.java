package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;

import static org.cloudwarp.probablychests.world.gen.SurfaceChestGeneration.*;

import java.util.Optional;
import java.util.Random;

public class SurfaceChestFeature extends Feature<DefaultFeatureConfig> {
	public SurfaceChestFeature (Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<DefaultFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		DefaultFeatureConfig config = context.getConfig();
		BlockState blockToBePlaced = null;
		// Set block type based on environment
		if (! structureWorldAccess.getBlockState(pos.down()).isSolidBlock(structureWorldAccess, pos)) {
			return false;
		}
		Biome biome = structureWorldAccess.getBiome(pos).value();

		if (structureWorldAccess.getDimension().isUltrawarm()) {
			return false;
		} else if (structureWorldAccess.getDimension().isRespawnAnchorWorking()) {
			return false;
		} else if (structureWorldAccess.getBiome(pos).isIn(BiomeTags.IS_OCEAN)) {
			return false;
		} else {
			if (structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.FLOWER_FORESTS) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE)) {
				blockToBePlaced = PCBlocks.LUSH_CHEST.getDefaultState();
			} else if (isBiomeWithinTempRange(biome, 1F, 10.0F) ||
					structureWorldAccess.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_HOT)) {
				blockToBePlaced = PCBlocks.ROCKY_CHEST.getDefaultState();
			} else {
				blockToBePlaced = PCBlocks.NORMAL_CHEST.getDefaultState();
			}
		}

		structureWorldAccess.setBlockState(pos, blockToBePlaced, 3);
		//structureWorldAccess.setBlockState(pos.up(), Blocks.SOUL_CAMPFIRE.getDefaultState(), 3);
		PCChestBlockEntity chest = (PCChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
		}
		return true;
	}

	public static boolean isBiomeWithinTempRange (Biome biome, float minTemp, float maxTemp) {
		return biome.getTemperature() >= minTemp && biome.getTemperature() < maxTemp;
	}
}
