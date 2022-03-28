package org.cloudwarp.probablychests.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.registry.ModBlocks;

public class MobScarecrowBlockEntity extends BlockEntity {
	public MobScarecrowBlockEntity (BlockPos pos, BlockState state) {
		super(ModBlocks.SCARECROW_BLOCK_ENTITY, pos, state);
	}

}
