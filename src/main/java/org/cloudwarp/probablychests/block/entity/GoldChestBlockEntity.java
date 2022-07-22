package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class GoldChestBlockEntity extends PCBaseChestBlockEntity {

	public GoldChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.GOLD, pos, state);
	}
}
