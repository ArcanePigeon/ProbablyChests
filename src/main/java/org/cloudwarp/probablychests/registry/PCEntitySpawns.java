package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;

public class PCEntitySpawns {

	public static void init(){
		PCConfig config = ProbablyChests.loadedConfig;
		if(config.mimicSettings.spawnNaturalMimics) {
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.NORMAL_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.ROCKY_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.LUSH_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.GOLD_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.STONE_CHEST_MIMIC, 1, 1, 1);
		}
	}
}
