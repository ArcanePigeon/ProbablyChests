package org.cloudwarp.mobscarecrow.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

public class SkeletonScarecrowBlockEntity extends BlockEntity {
	public SkeletonScarecrowBlockEntity (BlockPos pos, BlockState state) {
		super(ModBlocks.SKELETON_SCARECROW_BLOCK_ENTITY, pos, state);
	}
}
