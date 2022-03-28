package org.cloudwarp.probablychests.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.registry.ModBlocks;

public class SkeletonScarecrowBigBlockEntity extends BlockEntity {
	public SkeletonScarecrowBigBlockEntity (BlockPos pos, BlockState state) {
		super(ModBlocks.SKELETON_SCARECROW_BIG_BLOCK_ENTITY, pos, state);
	}
}
