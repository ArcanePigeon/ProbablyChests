package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class IceChestBlockEntity extends PCChestBlockEntity {

	public IceChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.ICE, pos, state);
	}
}
