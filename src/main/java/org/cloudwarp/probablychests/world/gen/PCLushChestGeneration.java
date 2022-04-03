package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class PCLushChestGeneration {
	public static void generateChest() {
		BiomeModifications.addFeature(BiomeSelectors.all(),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.LUSH_CHEST_PLACED.getKey().get());
	}
}
