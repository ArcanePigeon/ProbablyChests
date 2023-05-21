package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.cloudwarp.probablychests.data.PCWorldGenerator;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class PCDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(PCWorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, PCFeatures::bootstrapConfigured);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PCFeatures::bootstrapPlaced);
	}
}
