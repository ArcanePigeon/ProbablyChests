package org.cloudwarp.mobscarecrow.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

public class ZombieScarecrowBlockEntity extends BlockEntity {
	public ZombieScarecrowBlockEntity (BlockPos pos, BlockState state) {
		super(ModBlocks.ZOMBIE_SCARECROW_BLOCK_ENTITY, pos, state);
	}
}