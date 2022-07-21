package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class NetherChestBlockEntity extends PCChestBlockEntity {

	public NetherChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.NETHER, pos, state);
	}
}
