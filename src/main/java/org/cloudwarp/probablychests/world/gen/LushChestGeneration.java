package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class LushChestGeneration {
	public static void generateChest () {
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context ->{
			Biome biome = context.getBiome();
			return biome.getTemperature() < 1.0f && biome.getTemperature() >= 0.5f;
				}).and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.DRIPSTONE_CAVES,BiomeKeys.OCEAN,BiomeKeys.COLD_OCEAN,BiomeKeys.DEEP_COLD_OCEAN,
						BiomeKeys.DEEP_FROZEN_OCEAN,BiomeKeys.DEEP_OCEAN,BiomeKeys.DEEP_LUKEWARM_OCEAN,BiomeKeys.FROZEN_OCEAN,
						BiomeKeys.LUKEWARM_OCEAN,BiomeKeys.WARM_OCEAN))),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.LUSH_CHEST_PLACED.getKey().get());
	}
}
