package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.Config;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;
import java.util.function.Predicate;

public class SurfaceChestGeneration {

	public static final Predicate<BiomeSelectionContext> LushChestSurfaceBiomeSelector = BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
		Biome biome = context.getBiome();
		return biome.getTemperature() < 1.0f && biome.getTemperature() >= 0.5f;
	}).and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.OCEAN, BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN,
			BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.FROZEN_OCEAN,
			BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN)));

	public static final Predicate<BiomeSelectionContext> NormalChestSurfaceBiomeSelector = BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
		Biome biome = context.getBiome();
		return biome.getTemperature() < 0.5f;
	});

	public static final Predicate<BiomeSelectionContext> RockyChestSurfaceBiomeSelector = BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
		Biome biome = context.getBiome();
		return biome.getTemperature() >= 1.0f;
	});

	public static void generateChest () {
		Config config = ProbablyChests.config;
		float surfaceChestRarity = config.getSurfaceChestSpawnChance();
		if (surfaceChestRarity > 0) {
			BiomeModifications.addFeature(LushChestSurfaceBiomeSelector, GenerationStep.Feature.SURFACE_STRUCTURES, PCFeatures.LUSH_CHEST_PLACED_SURFACE.getKey().get());
			//---------------------------------------------------
			BiomeModifications.addFeature(NormalChestSurfaceBiomeSelector, GenerationStep.Feature.SURFACE_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED_SURFACE.getKey().get());
			//---------------------------------------------------
			BiomeModifications.addFeature(RockyChestSurfaceBiomeSelector, GenerationStep.Feature.SURFACE_STRUCTURES, PCFeatures.ROCKY_CHEST_PLACED_SURFACE.getKey().get());
			//---------------------------------------------------
		}
	}
}
