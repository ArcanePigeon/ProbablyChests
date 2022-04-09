package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;

public class PCEntities {
	public static final EntityType<PCChestMimic> NORMAL_CHEST_MIMIC = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "normal_chest_mimic"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimic::new).dimensions(EntityDimensions.fixed(1f, 1.1f)).build()
	);
	public static final EntityType<PCChestMimic> LUSH_CHEST_MIMIC = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "lush_chest_mimic"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimic::new).dimensions(EntityDimensions.fixed(1f, 1.1f)).build()
	);
	public static final EntityType<PCChestMimic> ROCKY_CHEST_MIMIC = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "rocky_chest_mimic"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimic::new).dimensions(EntityDimensions.fixed(1f, 1.1f)).build()
	);

	public static void init(){
		FabricDefaultAttributeRegistry.register(NORMAL_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(LUSH_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(ROCKY_CHEST_MIMIC, PCChestMimic.createMobAttributes());
	}
}
