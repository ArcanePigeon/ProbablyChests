package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

public class StepOnScarecrowBlockGoal extends MoveToTargetPosGoal {
	private final Tag.Identified<Block> targetTag;
	private final MobEntity stepAndDestroyMob;

	public StepOnScarecrowBlockGoal (Tag.Identified<Block> targetTag, PathAwareEntity mob, double speed, int maxYDifference) {
		super(mob, speed, 24, maxYDifference);
		this.targetTag = targetTag;
		this.stepAndDestroyMob = mob;
	}

	public boolean canStart () {
		if (this.cooldown > 0) {
			-- this.cooldown;
			return false;
		} else if (this.hasAvailableTarget()) {
			this.cooldown = toGoalTicks(20);
			return true;
		} else {
			this.cooldown = this.getInterval(this.mob);
			return false;
		}
	}

	private boolean hasAvailableTarget () {
		return this.targetPos != null && this.isTargetPos(this.mob.world, this.targetPos) ? true : this.findTargetPos();
	}

	public void stop () {
		super.stop();
		this.stepAndDestroyMob.fallDistance = 1.0F;
	}

	public void start () {
		super.start();
	}

	public void tick () {
		super.tick();
	}

	protected boolean isTargetPos (WorldView world, BlockPos pos) {
		Chunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
		if (chunk == null) {
			return false;
		} else {
			return chunk.getBlockState(pos).isIn(targetTag) && chunk.getBlockState(pos.up()).isAir() && chunk.getBlockState(pos.up(2)).isAir();
		}
	}
}
