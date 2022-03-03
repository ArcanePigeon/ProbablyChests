package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class AvoidScarecrowGoal extends Goal {
	private LivingEntity entity;
	private float speed = 1.5F;
	/** The PathEntity of our entity */
	private Path path;
	/** The PathNavigate of our entity */
	private EntityNavigation navigation;
	private PathAwareEntity pathEntity;
	private boolean pathable = false;
	private BlockPos pos;
	private int pathTimeLimiter;
	private int scarecrowCheckLimiter;
	private Tag.Identified<Block> tag;

	public AvoidScarecrowGoal (LivingEntity e, Tag.Identified<Block> scarecrowTag) {
		entity = e;
		tag = scarecrowTag;
		if (entity instanceof PathAwareEntity) {
			pathable = true;
			pathEntity = (PathAwareEntity) entity;
			navigation = pathEntity.getNavigation();
		}
	}

	@Override
	public boolean shouldContinue () {
		return ! path.isFinished() && pathTimeLimiter > 0;
	}

	@Override
	public boolean canStart () {
		scarecrowCheckLimiter = Math.max(scarecrowCheckLimiter - 1, 0);
		checkForScarecrow();
		if (pos != null && pathable && EntityUtils.isScarecrowAround(entity, pos)) {
			return generatePath();
		}
		return false;
	}

	private void checkForScarecrow () {
		if (scarecrowCheckLimiter <= 0) {
			Optional<BlockPos> foundPos = EntityUtils.findNearestScarecrow(entity.getEntityWorld(), entity, tag);
			pos = foundPos.isPresent() ? foundPos.get() : pos;
			scarecrowCheckLimiter = 60;
		}
	}

	private boolean generatePath () {
		Vec3d scarecrowPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
		for (int i = 0; i < 10; i++) {
			Vec3d newPos = NoPenaltyTargeting.findFrom(pathEntity, 8, 6, scarecrowPos);
			if (newPos == null) {
				continue;
			}
			if (newPos.squaredDistanceTo(scarecrowPos) < entity.squaredDistanceTo(scarecrowPos)) {
				continue;
			}
			path = navigation.findPathTo(new BlockPos(newPos), 0);
			if (path != null) {
				pathTimeLimiter = 5;
				return true;
			}
		}
		return false;
	}

	@Override
	public void start () {
		navigation.startMovingAlong(path, speed);
	}

	@Override
	public void tick () {
		pathTimeLimiter = Math.max(pathTimeLimiter - 1, 0);
	}
}
