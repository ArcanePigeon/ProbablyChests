package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;

public class PCEntities {
	public static final EntityType<PCChestMimic> NORMAL_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "normal_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> LUSH_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "lush_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> ROCKY_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "rocky_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> STONE_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "stone_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> GOLD_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "gold_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> NETHER_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "nether_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> SHADOW_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "shadow_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> ICE_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "ice_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimic> CORAL_CHEST_MIMIC = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "coral_chest_mimic"),
			FabricEntityTypeBuilder.createMob().entityFactory(PCChestMimic::new).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,PCChestMimic::canSpawn).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	//---------------------------------------
	public static final EntityType<PCChestMimicPet> NORMAL_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "normal_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> LUSH_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "lush_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> ROCKY_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "rocky_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> STONE_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "stone_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> GOLD_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "gold_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> NETHER_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "nether_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> SHADOW_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "shadow_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> ICE_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "ice_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);
	public static final EntityType<PCChestMimicPet> CORAL_CHEST_MIMIC_PET = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(ProbablyChests.MOD_ID, "coral_chest_mimic_pet"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PCChestMimicPet::new).dimensions(EntityDimensions.fixed(0.9f, 0.9f)).build()
	);

	public static void init () {
		FabricDefaultAttributeRegistry.register(NORMAL_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(LUSH_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(ROCKY_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(STONE_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(GOLD_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(NETHER_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(SHADOW_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(ICE_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		FabricDefaultAttributeRegistry.register(CORAL_CHEST_MIMIC, PCChestMimic.createMobAttributes());
		//------------------------------
		FabricDefaultAttributeRegistry.register(NORMAL_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(LUSH_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(ROCKY_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(STONE_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(GOLD_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(NETHER_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(SHADOW_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(ICE_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
		FabricDefaultAttributeRegistry.register(CORAL_CHEST_MIMIC_PET, PCChestMimicPet.createMobAttributes());
	}
}
