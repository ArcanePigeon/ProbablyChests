package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class NormalChestBlockEntity extends PCBaseChestBlockEntity {

	public NormalChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.NORMAL, pos, state);
	}
}
