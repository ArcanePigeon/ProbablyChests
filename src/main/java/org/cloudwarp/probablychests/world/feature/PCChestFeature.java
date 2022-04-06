package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.registry.PCBiomeTags;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCLootTables;

import java.util.Random;

public class PCChestFeature extends Feature<PCChestFeatureConfig> {
	public PCChestFeature (Codec<PCChestFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<PCChestFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		PCChestFeatureConfig config = context.getConfig();
		if(structureWorldAccess.getBiome(pos).isIn(PCBiomeTags.IS_LUSH)){
			structureWorldAccess.setBlockState(pos, PCBlocks.LUSH_CHEST.getDefaultState(), 3);
			LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, pos, PCLootTables.LUSH_CHEST);
		}else if(structureWorldAccess.getBiome(pos).isIn(PCBiomeTags.IS_DRIPSTONE)){
			structureWorldAccess.setBlockState(pos, PCBlocks.ROCKY_CHEST.getDefaultState(), 3);
			LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, pos, PCLootTables.LUSH_CHEST);
		}else{
			structureWorldAccess.setBlockState(pos, PCBlocks.NORMAL_CHEST.getDefaultState(), 3);
			LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, pos, PCLootTables.LUSH_CHEST);
		}

		return true;
	}
}
