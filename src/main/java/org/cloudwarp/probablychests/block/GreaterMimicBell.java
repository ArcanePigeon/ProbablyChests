package org.cloudwarp.probablychests.block;

import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Attachment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class GreaterMimicBell extends BellBlock {
	public GreaterMimicBell (Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return this.ring(world, state, hit, player, true) ? ActionResult.success(world.isClient) : ActionResult.PASS;
	}
	@Override
	public boolean ring(World world, BlockState state, BlockHitResult hitResult, @Nullable PlayerEntity player, boolean bl) {
		boolean bl2;
		Direction direction = hitResult.getSide();
		BlockPos blockPos = hitResult.getBlockPos();
		boolean bl3 = bl2 = !bl || this.isPointOnBell(state, direction, hitResult.getPos().y - (double)blockPos.getY());
		if (bl2) {
			boolean bl32 = this.ring(player, world, blockPos, direction);
			if (bl32 && player != null) {
				player.incrementStat(Stats.BELL_RING);
			}
			return true;
		}
		return false;
	}
	private boolean isPointOnBell(BlockState state, Direction side, double y) {
		if (side.getAxis() == Direction.Axis.Y || y > (double)0.8124f) {
			return false;
		}
		Direction direction = state.get(FACING);
		Attachment attachment = state.get(ATTACHMENT);
		switch (attachment) {
			case FLOOR: {
				return direction.getAxis() == side.getAxis();
			}
			case SINGLE_WALL:
			case DOUBLE_WALL: {
				return direction.getAxis() != side.getAxis();
			}
			case CEILING: {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean ring(World world, BlockPos pos, @Nullable Direction direction) {
		return this.ring(null, world, pos, direction);
	}
	@Override
	public boolean ring(@Nullable Entity entity, World world, BlockPos pos, @Nullable Direction direction) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!world.isClient && blockEntity instanceof BellBlockEntity) {
			if (direction == null) {
				direction = world.getBlockState(pos).get(FACING);
			}
			((BellBlockEntity)blockEntity).activate(direction);
			world.playSound(null, pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f);
			world.emitGameEvent(entity, GameEvent.RING_BELL, pos);
			return true;
		}
		return false;
	}
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BellBlockEntity(pos, state);
	}
}
