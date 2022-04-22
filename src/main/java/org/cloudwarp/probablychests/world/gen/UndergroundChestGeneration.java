package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.Config;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class UndergroundChestGeneration {
	public static void generateChest () {
		Config config = ProbablyChests.config;
		float chestRarity = config.getChestSpawnChance();
		if (chestRarity > 0) {
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.GOLD_CHEST_PLACED.getKey().get());
			//---------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld())
							.and(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES)),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.LUSH_CHEST_PLACED.getKey().get());
			//---------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED.getKey().get());
			//---------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInTheNether()),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED.getKey().get());
			//---------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld())
							.and(BiomeSelectors.includeByKey(List.of(BiomeKeys.DRIPSTONE_CAVES))),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.ROCKY_CHEST_PLACED.getKey().get());
			//---------------------------
			BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
					GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.STONE_CHEST_PLACED.getKey().get());
			//---------------------------
			//---------------------------
			//---------------------------
		}
	}
}
