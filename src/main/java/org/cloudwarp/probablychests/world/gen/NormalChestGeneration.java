package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class NormalChestGeneration {
	public static void generateChest () {
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED.getKey().get());
		//---------------------------------------------------
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()).and(context -> {
					Biome biome = context.getBiome();
					return biome.getTemperature() < 0.5f;
				}),
				GenerationStep.Feature.SURFACE_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED_SURFACE.getKey().get());
		//---------------------------------------------------
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInTheNether()),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_CHEST_PLACED.getKey().get());
	}
}
