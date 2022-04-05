package org.cloudwarp.probablychests.registry;

import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class PCVoxelShapes {
	public static final VoxelShape POT_VOXELSHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(3, 12, 3, 13, 16, 13), Block.createCuboidShape(1, 0, 1, 15, 12, 15), BooleanBiFunction.OR);
}
