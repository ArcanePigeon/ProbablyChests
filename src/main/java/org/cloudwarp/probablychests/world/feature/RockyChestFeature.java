package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCLootTables;

import java.util.Random;

public class RockyChestFeature extends Feature<PCChestFeatureConfig> {
	public RockyChestFeature (Codec<PCChestFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<PCChestFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		PCChestFeatureConfig config = context.getConfig();
		structureWorldAccess.setBlockState(pos, PCBlocks.ROCKY_CHEST.getDefaultState(), 3);
		//structureWorldAccess.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState(), 3);
		PCChestBlockEntity chest = (PCChestBlockEntity) structureWorldAccess.getBlockEntity(pos);
		if (chest != null) {
			chest.isNatural = true;
		}
		//LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, pos, PCLootTables.ROCKY_CHEST);

		return true;
	}
}
