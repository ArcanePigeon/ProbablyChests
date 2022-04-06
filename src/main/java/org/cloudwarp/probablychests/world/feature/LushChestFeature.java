package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCLootTables;

import java.util.Random;

public class LushChestFeature extends Feature<PCChestFeatureConfig> {
	public LushChestFeature (Codec<PCChestFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<PCChestFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		PCChestFeatureConfig config = context.getConfig();
		structureWorldAccess.setBlockState(pos, PCBlocks.LUSH_CHEST.getDefaultState(), 3);
		//structureWorldAccess.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState(), 3);
		LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, pos, PCLootTables.LUSH_CHEST);

		return true;
	}
}
