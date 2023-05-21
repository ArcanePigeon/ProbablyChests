package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class PCPotGeneration {
	/*public static void generatePot () {
		PCConfig config = ProbablyChests.loadedConfig;
		float potRarity = config.worldGen.potSpawnChance;
		if (potRarity > 0) {
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
						Biome biome = context.getBiome();
						return biome.getTemperature() < 1.0f && biome.getTemperature() >= 0.5f;
					}).and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.DRIPSTONE_CAVES, BiomeKeys.OCEAN, BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN,
							BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.FROZEN_OCEAN,
							BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN))),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.LUSH_POT_PLACED.getKey().get());
			//-------------------------------------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
						Biome biome = context.getBiome();
						return biome.getTemperature() <= 0.5f  || (biome.getTemperature() == 0.8 && biome.getDownfall() == 0.4f);
					}).and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.LUSH_CAVES, BiomeKeys.DRIPSTONE_CAVES))),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_POT_PLACED.getKey().get());
			//--------------------------------------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
						Biome biome = context.getBiome();
						return biome.getTemperature() >= 1.0f || (biome.getTemperature() == 0.8f && biome.getDownfall() == 0.4f);
					}).and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.BEACH))),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.ROCKY_POT_PLACED.getKey().get());
			//------------------------------------------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInTheNether()),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NETHER_POT_PLACED.getKey().get());
		}
	}*/
}
