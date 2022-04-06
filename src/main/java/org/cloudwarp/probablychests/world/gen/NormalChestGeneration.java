package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class NormalChestGeneration {
	public static void generateChest () {
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(List.of(BiomeKeys.LUSH_CAVES,BiomeKeys.DRIPSTONE_CAVES))),
				//BiomeModifications.addFeature(BiomeSelectors.all(),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED.getKey().get());
	}
}
