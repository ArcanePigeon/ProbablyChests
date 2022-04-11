package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

import java.util.List;

public class StoneChestGeneration {
	public static void generateChest () {
		BiomeModifications.addFeature(BiomeSelectors.all().and(BiomeSelectors.foundInOverworld()),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.STONE_CHEST_PLACED.getKey().get());
	}
}
