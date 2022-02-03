package org.cloudwarp.mobscarecrow.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

public class SpiderScarecrowBlockEntity extends BlockEntity {
	public SpiderScarecrowBlockEntity (BlockPos pos, BlockState state) {
		super(ModBlocks.SPIDER_SCARECROW_BLOCK_ENTITY, pos, state);
	}
}
