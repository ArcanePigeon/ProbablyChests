package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class StoneChestBlockEntity extends PCChestBlockEntity {

	public StoneChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.STONE, pos, state);
	}
}
