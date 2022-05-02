package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.cloudwarp.probablychests.utils.Config;
import org.cloudwarp.probablychests.world.gen.SurfaceChestGeneration;

import java.util.List;
import java.util.function.Predicate;

public class PCEntitySpawns {

	public static void init(){
		Config config = Config.getInstance();
		if(config.getDoSpawnNaturalMimics()) {
			BiomeModifications.addSpawn(SurfaceChestGeneration.NormalChestSurfaceBiomeSelector, SpawnGroup.MONSTER, PCEntities.NORMAL_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(SurfaceChestGeneration.RockyChestSurfaceBiomeSelector, SpawnGroup.MONSTER, PCEntities.ROCKY_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(SurfaceChestGeneration.LushChestSurfaceBiomeSelector, SpawnGroup.MONSTER, PCEntities.LUSH_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.GOLD_CHEST_MIMIC, 1, 1, 1);
			BiomeModifications.addSpawn(BiomeSelectors.spawnsOneOf(EntityType.ZOMBIE), SpawnGroup.MONSTER, PCEntities.STONE_CHEST_MIMIC, 1, 1, 1);
		}
	}
}
