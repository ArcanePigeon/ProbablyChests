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
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES)),
		//BiomeModifications.addFeature(BiomeSelectors.all(),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.LUSH_CHEST_PLACED.getKey().get());
	}
}
