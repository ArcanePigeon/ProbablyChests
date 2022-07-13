package org.cloudwarp.probablychests.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.utils.MimicDifficulty;
import org.cloudwarp.probablychests.utils.PCConfig;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class PCChestMimic extends PCTameablePetWithInventory implements IAnimatable, Monster {
	// Animations
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", true);
	public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("jump", false).addAnimation("flying", true);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("land", false).addAnimation("idle", true);
	public static final AnimationBuilder SLEEPING = new AnimationBuilder().addAnimation("sleeping", true);
	public static final AnimationBuilder FLYING = new AnimationBuilder().addAnimation("flying", true);
	private static final String CONTROLLER_NAME = "mimicController";

	private static double moveSpeed = 1.5D;
	private static int maxHealth = 50;
	private static int maxDamage = 5;

	private AnimationFactory factory = new AnimationFactory(this);
	private boolean onGroundLastTick;
	private int timeUntilSleep = 0;
	private int jumpEndTimer = 10;
	private int spawnWaitTimer = 10;
	private boolean isAttemptingToSleep = false;

	public PCChestMimic (EntityType<? extends PCTameablePetWithInventory> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
		this.moveControl = new PCChestMimic.MimicMoveControl(this);
		this.experiencePoints = 10;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		MimicDifficulty mimicDifficulty = ProbablyChests.loadedConfig.mimicSettings.mimicDifficulty;
		moveSpeed = mimicDifficulty.getSpeed();
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, mimicDifficulty.getDamage())
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, mimicDifficulty.getHealth())
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
	}


	protected void initGoals () {
		this.goalSelector.add(2, new PCChestMimic.FaceTowardTargetGoal(this));
		this.goalSelector.add(7, new PCChestMimic.IdleGoal(this));
		this.goalSelector.add(6, new PCChestMimic.SleepGoal(this));
		this.goalSelector.add(1, new PCChestMimic.SwimmingGoal(this));
		this.goalSelector.add(5, new PCChestMimic.MoveGoal(this));
		this.targetSelector.add(3, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
	}

	private <E extends IAnimatable> PlayState devMovement (AnimationEvent<E> animationEvent) {
		int state = this.getMimicState();
		animationEvent.getController().setAnimationSpeed(1D);
		switch (state) {
			case IS_SLEEPING:
				animationEvent.getController().setAnimation(SLEEPING);
				break;
			case IS_IN_AIR:
				animationEvent.getController().setAnimation(FLYING);
				break;
			case IS_CLOSED:
				animationEvent.getController().setAnimation(CLOSE);
				break;
			case IS_IDLE:
				animationEvent.getController().setAnimation(IDLE);
				break;
			case IS_JUMPING:
				animationEvent.getController().setAnimationSpeed(2D);
				animationEvent.getController().setAnimation(JUMP);
				break;
			default:
				animationEvent.getController().setAnimation(SLEEPING);
				break;
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, CONTROLLER_NAME, 6, this::devMovement));
	}

	@Override
	public AnimationFactory getFactory () {
		return this.factory;
	}


	protected void jump () {
		Vec3d vec3d = this.getVelocity();
		LivingEntity livingEntity = this.getTarget();
		double jumpStrength;
		if (livingEntity == null) {
			jumpStrength = 1D;
		} else {
			jumpStrength = livingEntity.getY() - this.getY();
			jumpStrength = jumpStrength <= 0 ? 1.0D : Math.min(jumpStrength / 3.5D + 1.0D, 2.5D);
		}
		this.setVelocity(vec3d.x, (double) this.getJumpVelocity() * jumpStrength, vec3d.z);
		this.velocityDirty = true;
		if (this.isOnGround() && this.jumpEndTimer <= 0) {
			this.jumpEndTimer = 10;
			this.setMimicState(IS_JUMPING);
		}
	}

	protected boolean canAttack () {
		return this.canMoveVoluntarily();
	}

	protected float getDamageAmount () {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
	}

	public void onPlayerCollision (PlayerEntity player) {
		if (this.canAttack()) {
			this.damage(player);
		}

	}

	protected void damage (LivingEntity target) {
		if (this.isAlive()) {
			if (this.squaredDistanceToEntity(target) < 0.6D && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
				this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
				this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
				this.applyDamageEffects(this, target);
			}
		}
	}

	public double squaredDistanceToEntity(LivingEntity entity) {
		Vec3d vector  = entity.getPos();
		double d = this.getX() - vector.x;
		double e = this.getY() - (vector.y+1);
		double f = this.getZ() - vector.z;
		return d * d + e * e + f * f;
	}

	protected int getTicksUntilNextJump () {
		return this.random.nextInt(40) + 5;
	}

	public void tick () {
		super.tick();
		if (jumpEndTimer >= 0) {
			jumpEndTimer -= 1;
		}if (spawnWaitTimer > 0) {
			spawnWaitTimer -= 1;
		} else {
			if (this.onGround) {
				if (this.onGroundLastTick) {
					if (this.getMimicState() != IS_SLEEPING && ! isAttemptingToSleep) {
						timeUntilSleep = 150;
						isAttemptingToSleep = true;
						if (this.getMimicState() != IS_CLOSED) {
							this.setMimicState(IS_IDLE);
						}
					}
					if (isAttemptingToSleep) {
						timeUntilSleep -= 1;
						if (timeUntilSleep <= 0) {
							timeUntilSleep = 0;
							this.setMimicState(IS_SLEEPING);
						}
					}
				} else {
					isAttemptingToSleep = false;
					this.setMimicState(IS_CLOSED);
					this.playSound(this.getLandingSound(), this.getSoundVolume(),
							((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
				}
			} else {
				isAttemptingToSleep = false;
				if (this.getMimicState() != IS_JUMPING) {
					this.setMimicState(IS_IN_AIR);
				}
			}
		}
		this.onGroundLastTick = this.onGround;
	}

	protected boolean isDisallowedInPeaceful () {
		return true;
	}

	public void writeCustomDataToNbt (NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("wasOnGround", this.onGroundLastTick);
	}

	public void readCustomDataFromNbt (NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.onGroundLastTick = nbt.getBoolean("wasOnGround");
	}

	@Override
	protected void initDataTracker () {
		this.dataTracker.startTracking(getMimicStateVariable(), IS_SLEEPING);
		super.initDataTracker();
	}

	public boolean cannotDespawn () {
		return this.hasVehicle() || ! this.inventory.isEmpty();
	}

	public static boolean isSpawnDark (ServerWorldAccess world, BlockPos pos, Random random) {
		if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
			return false;
		} else if (world.getLightLevel(LightType.BLOCK, pos) > 0) {
			return false;
		} else {
			PCConfig config = ProbablyChests.loadedConfig;
			int i = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
			return i <= random.nextInt(8) * config.mimicSettings.naturalMimicSpawnRate;
		}
	}

	public static boolean canSpawn (EntityType<PCChestMimic> pcChestMimicEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
		if (serverWorldAccess.isSkyVisible(blockPos) || isSpawnDark(serverWorldAccess, blockPos, random)) {
			return false;
		}
		return true;
	}

	private static class MimicMoveControl extends MoveControl {
		private final PCChestMimic mimic;
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public MimicMoveControl (PCChestMimic mimic) {
			super(mimic);
			this.mimic = mimic;
			this.targetYaw = 180.0F * mimic.getYaw() / 3.1415927F;
		}

		public void look (float targetYaw, boolean jumpOften) {
			this.targetYaw = targetYaw;
			this.jumpOften = jumpOften;
		}

		public void move (double speed) {
			this.speed = speed;
			this.state = State.MOVE_TO;
		}

		public void tick () {
			this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
			this.entity.headYaw = this.entity.getYaw();
			this.entity.bodyYaw = this.entity.getYaw();
			if (this.state != State.MOVE_TO) {
				this.entity.setForwardSpeed(0.0F);
			} else {
				this.state = State.WAIT;
				if (this.entity.isOnGround()) {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
					if (this.ticksUntilJump-- <= 0) {
						this.ticksUntilJump = this.mimic.getTicksUntilNextJump();
						if (this.jumpOften) {
							this.ticksUntilJump /= 3;
						}

						this.mimic.getJumpControl().setActive();
						this.mimic.playSound(this.mimic.getJumpSound(), this.mimic.getSoundVolume(), this.mimic.getJumpSoundPitch());
					} else {
						this.mimic.sidewaysSpeed = 0.0F;
						this.mimic.forwardSpeed = 0.0F;
						this.entity.setMovementSpeed(0.0F);
					}
				} else {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				}

			}
		}
	}

	static class FaceTowardTargetGoal extends Goal {
		private final PCChestMimic mimic;
		private int ticksLeft;

		public FaceTowardTargetGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.LOOK));
		}

		public boolean canStart () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else {
				return ! this.mimic.canTarget(livingEntity) ? false : this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
			}
		}

		public void start () {
			this.ticksLeft = toGoalTicks(300);
			super.start();
		}

		public boolean shouldContinue () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (! this.mimic.canTarget(livingEntity)) {
				return false;
			} else {
				return -- this.ticksLeft > 0;
			}
		}

		public boolean shouldRunEveryTick () {
			return true;
		}

		public void tick () {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity != null) {
				this.mimic.lookAtEntity(livingEntity, 10.0F, 10.0F);
			}

			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYaw(), this.mimic.canAttack());
		}
	}

	static class MoveGoal extends Goal {
		private final PCChestMimic mimic;

		public MoveGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
		}

		public boolean canStart () {
			//return !this.mimc.hasVehicle();
			return this.mimic.getTarget() != null;
		}

		public void tick () {
			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).move(moveSpeed);
		}
	}

	static class SwimmingGoal extends Goal {
		private final PCChestMimic mimic;

		public SwimmingGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
			mimic.getNavigation().setCanSwim(true);
		}

		public boolean canStart () {
			return (this.mimic.isTouchingWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
		}

		public boolean shouldRunEveryTick () {
			return true;
		}

		public void tick () {
			if (this.mimic.getRandom().nextFloat() < 0.8F) {
				this.mimic.getJumpControl().setActive();
			}

			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).move(4.2D);
		}
	}

	static class IdleGoal extends Goal {
		private final PCChestMimic mimic;

		public IdleGoal (PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.LOOK));
		}

		public boolean canStart () {
			return ! this.mimic.hasVehicle();
		}

		public void tick () {

		}
	}
	static class SleepGoal extends Goal {
		private final PCChestMimic mimic;

		public SleepGoal (PCChestMimic mimic) {
			this.mimic = mimic;
		}

		public boolean canStart () {
			return ! this.mimic.hasVehicle() && this.mimic.getMimicState() == IS_SLEEPING;
		}
		public boolean shouldContinue(){
			return ! this.mimic.hasVehicle() && this.mimic.getMimicState() == IS_SLEEPING;
		}

		public void tick () {
			lockToBlock(10F,10F);
			((PCChestMimic.MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYaw(), true);
		}
		public void lockToBlock(float maxYawChange, float maxPitchChange) {
			float r = Math.round(this.mimic.getHeadYaw() / 90F)*90F;
			double x = this.mimic.getBlockX() - this.mimic.getX();
			double z = this.mimic.getBlockZ() - this.mimic.getZ();
			this.mimic.setYaw(this.changeAngle(this.mimic.getYaw(), r, maxYawChange));
		}
		private float changeAngle(float from, float to, float max) {
			float f = MathHelper.wrapDegrees(to - from);
			if (f > max) {
				f = max;
			}
			if (f < -max) {
				f = -max;
			}
			return from + f;
		}
	}

}
