package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class LushChestBlockEntity extends PCChestBlockEntity {

	public LushChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.LUSH, pos, state);
	}
}
