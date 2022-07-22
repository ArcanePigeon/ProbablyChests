package org.cloudwarp.probablychests.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

import java.util.EnumSet;

public class PCMeleeAttackGoal extends Goal {
	protected final PathAwareEntity mob;
	private final double speed;
	private final boolean pauseWhenMobIdle;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int updateCountdownTicks;
	private int cooldown;
	private final int attackIntervalTicks = 20;
	private long lastUpdateTime;
	private static final long MAX_ATTACK_TIME = 20L;
	protected final PCTameablePetWithInventory mimic;

	public PCMeleeAttackGoal (PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
		this.mob = mob;
		this.mimic = (PCTameablePetWithInventory) mob;
		this.speed = speed;
		this.pauseWhenMobIdle = pauseWhenMobIdle;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Control.JUMP));
	}

	@Override
	public boolean canStart () {
		long l = this.mob.world.getTime();
		if (l - this.lastUpdateTime < 20L) {
			return false;
		}
		this.lastUpdateTime = l;
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (this.mimic.getIsAbandoned() && this.mimic instanceof PCChestMimicPet) {
			return false;
		}
		if (! livingEntity.isAlive()) {
			return false;
		}
		if (this.mimic.getOwner() == livingEntity && this.mimic instanceof PCChestMimicPet) {
			return false;
		}
		this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
		if (this.path != null) {
			return true;
		}
		return this.getSquaredMaxAttackDistance(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
	}

	@Override
	public boolean shouldContinue () {
		System.out.println("HERE");
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return false;
		}
		if (! livingEntity.isAlive()) {
			return false;
		}
		if (this.mimic.getIsAbandoned() && this.mimic instanceof PCChestMimicPet) {
			return false;
		}
		if (this.mimic.getOwner() == livingEntity && this.mimic instanceof PCChestMimicPet) {
			return false;
		}
		if (! this.mob.isInWalkTargetRange(livingEntity.getBlockPos())) {
			return false;
		}
		return ! (livingEntity instanceof PlayerEntity) || ! livingEntity.isSpectator() && ! ((PlayerEntity) livingEntity).isCreative();
	}

	@Override
	public void start () {
		this.mob.setAttacking(true);
		this.updateCountdownTicks = 0;
		this.cooldown = 0;
	}

	@Override
	public void stop () {
		LivingEntity livingEntity = this.mob.getTarget();
		if (! EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.setTarget(null);
		}
		this.mob.setAttacking(false);
		this.mob.getNavigation().stop();
	}

	@Override
	public boolean shouldRunEveryTick () {
		return true;
	}

	@Override
	public void tick () {
		LivingEntity livingEntity = this.mob.getTarget();
		if (livingEntity == null) {
			return;
		}
		this.mimic.lookAtEntity(livingEntity, 10.0F, 10.0F);
		((MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYaw(), true);
		double d = this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
		if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
			this.targetX = livingEntity.getX();
			this.targetY = livingEntity.getY();
			this.targetZ = livingEntity.getZ();
			this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
			if (d > 1024.0) {
				this.updateCountdownTicks += 10;
			} else if (d > 256.0) {
				this.updateCountdownTicks += 5;
			}
			if (! this.mob.getNavigation().startMovingTo(livingEntity, this.speed)) {
				this.updateCountdownTicks += 15;
			}
			this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
		}
		this.cooldown = Math.max(this.cooldown - 1, 0);
		this.attack(livingEntity, d);
	}

	protected void attack (LivingEntity target, double squaredDistance) {
		double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d && this.cooldown <= 0) {
			this.resetCooldown();
			this.mob.tryAttack(target);
		}
	}

	protected void resetCooldown () {
		this.cooldown = this.getTickCount(20);
	}

	protected boolean isCooledDown () {
		return this.cooldown <= 0;
	}

	protected int getCooldown () {
		return this.cooldown;
	}

	protected int getMaxCooldown () {
		return this.getTickCount(20);
	}

	protected double getSquaredMaxAttackDistance (LivingEntity entity) {
		return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
	}
}

