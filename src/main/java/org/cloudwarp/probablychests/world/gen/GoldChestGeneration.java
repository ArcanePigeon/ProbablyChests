package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class GoldChestGeneration {
	public static void generateChest () {
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.GOLD_CHEST_PLACED.getKey().get());
	}
}
