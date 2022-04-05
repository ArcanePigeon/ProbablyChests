package org.cloudwarp.probablychests.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class PCNormalPotGeneration {
	public static void generatePot () {
		BiomeModifications.addFeature(BiomeSelectors.all(),
				GenerationStep.Feature.UNDERGROUND_STRUCTURES, PCFeatures.NORMAL_POT_PLACED.getKey().get());
	}
}
