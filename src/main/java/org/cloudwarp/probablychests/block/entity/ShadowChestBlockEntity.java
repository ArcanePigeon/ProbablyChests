package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.block.PCChestTypes;

public class ShadowChestBlockEntity extends PCBaseChestBlockEntity {

	public ShadowChestBlockEntity (BlockPos pos, BlockState state) {
		super(PCChestTypes.SHADOW, pos, state);
	}
}
