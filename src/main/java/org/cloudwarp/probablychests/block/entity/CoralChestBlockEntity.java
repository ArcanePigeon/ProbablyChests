package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class CoralChestBlockEntity extends PCBaseChestBlockEntity {

	public CoralChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.CORAL, pos, state);
	}
}
