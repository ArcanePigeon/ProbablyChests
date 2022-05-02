package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import org.cloudwarp.probablychests.utils.Config;

public class PCEntitySpawns {
	public static void init(){
		Config config = Config.getInstance();
		if(config.getDoSpawnNaturalMimics()) {
			BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER, PCEntities.NORMAL_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER, PCEntities.ROCKY_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER, PCEntities.LUSH_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER, PCEntities.GOLD_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.MONSTER, PCEntities.STONE_CHEST_MIMIC, 1, 1, 1);
		}
	}
}
