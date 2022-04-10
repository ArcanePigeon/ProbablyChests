package org.cloudwarp.probablychests.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;

public class PCChestMimic extends PathAwareEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", true)
			.addAnimation("flying",true);
	public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("jump", false);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("close", false).addAnimation("idle", true);
	public static final AnimationBuilder SLEEPING = new AnimationBuilder().addAnimation("sleeping", true);
	public static final AnimationBuilder FLYING = new AnimationBuilder().addAnimation("flying", true);
	private static final String CONTROLLER_NAME = "mimicController";
	private boolean onGroundLastTick;
	private static final TrackedData<Boolean> IS_JUMPING;
	private static final TrackedData<Boolean> IS_IDLE;
	private static final TrackedData<Boolean> IS_SLEEPING;
	private static final TrackedData<Boolean> IS_GROUNDED;
	private static final TrackedData<Boolean> IS_FLYING;
	private int timeUntilSleep = 0;
	private int jumpEndTimer = 10;
	private static double moveSpeed = 1.5D;
	private boolean isJumpAnimationPlaying = false;
	private boolean isJumpAnimationFinished = false;
	private int spawnWaitTimer = 8;


	/*
	TODO: reduce fall damage
	TODO: make not drown
	TODO: make it able to jump much higher
	TODO: make fire resistant
	TODO: add locking to block pos to be like a normal chest
	TODO: add time between jumps
	fix attacking through shield
	add new chest
	add new feature spawn mechanics
	add on hit and on explosion for spawning mimic
	add wand to make a chest a mimic
	add configs
	 */
	public PCChestMimic(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
		this.moveControl = new PCChestMimic.MimicMoveControl(this);
	}

	protected void initGoals() {
		this.goalSelector.add(2, new PCChestMimic.FaceTowardTargetGoal(this));
		this.goalSelector.add(7, new PCChestMimic.IdleGoal(this));
		this.goalSelector.add(1, new PCChestMimic.SwimmingGoal(this));
		this.goalSelector.add(5, new PCChestMimic.MoveGoal(this));
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
	}

	static {
		IS_JUMPING = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_IDLE = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_SLEEPING = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_GROUNDED = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_FLYING = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.BOOLEAN);
	}


	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED,1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH,50)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0.5D);
	}

	@Override
	public int getSafeFallDistance() {
		return 20;
	}

	@Override
	protected int computeFallDamage(float fallDistance, float damageMultiplier) {
		StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
		float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.getAmplifier() + 1);
		return MathHelper.ceil((fallDistance - 20.0F - f) * damageMultiplier);
	}

	public void printStates(){
		System.out.println(isJumping() + "  " + isFlying() + "  " + isSleeping() + "  " + isGrounded() + "  " + isIdle());
	}

	private <E extends IAnimatable> PlayState devMovement(AnimationEvent<E> animationEvent) {
		printStates();
		if(isJumping() && !this.isJumpAnimationFinished){
			if(!this.isJumpAnimationPlaying){
				animationEvent.getController().setAnimationSpeed(2D);
				animationEvent.getController().setAnimation(JUMP);
				this.isJumpAnimationPlaying = true;
			}else{
				if(animationEvent.getController().getAnimationState() == AnimationState.Stopped){
					animationEvent.getController().setAnimationSpeed(1D);
					this.isJumpAnimationPlaying = false;
					this.isJumpAnimationFinished = true;
				}
			}
		}else if(this.isFlying()){
			animationEvent.getController().setAnimation(FLYING);
		}else if(this.isSleeping()){
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(SLEEPING);
		} else if(this.isIdle()){
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(IDLE);
		}else if(this.isGrounded()){
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(CLOSE);
		}
		if(animationEvent.getController().getCurrentAnimation() == null){
		animationEvent.getController().setAnimation(SLEEPING);
		}
		return PlayState.CONTINUE;
	}


	@Override
	public void registerControllers (AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, CONTROLLER_NAME, 0, this::devMovement));
	}

	@Override
	public AnimationFactory getFactory () {
		return this.factory;
	}
	private static class MimicMoveControl extends MoveControl {
		private float targetYaw;
		private int ticksUntilJump;
		private final PCChestMimic mimic;
		private boolean jumpOften;

		public MimicMoveControl(PCChestMimic mimic) {
			super(mimic);
			this.mimic = mimic;
			this.targetYaw = 180.0F * mimic.getYaw() / 3.1415927F;
		}

		public void look(float targetYaw, boolean jumpOften) {
			this.targetYaw = targetYaw;
			this.jumpOften = jumpOften;
		}

		public void move(double speed) {
			this.speed = speed;
			this.state = State.MOVE_TO;
		}

		public void tick() {
			this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
			this.entity.headYaw = this.entity.getYaw();
			this.entity.bodyYaw = this.entity.getYaw();
			if (this.state != State.MOVE_TO) {
				this.entity.setForwardSpeed(0.0F);
			} else {
				this.state = State.WAIT;
				if (this.entity.isOnGround()) {
					this.entity.setMovementSpeed((float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
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
					this.entity.setMovementSpeed((float)(this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				}

			}
		}
	}

	static class FaceTowardTargetGoal extends Goal {
		private final PCChestMimic mimic;
		private int ticksLeft;

		public FaceTowardTargetGoal(PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.LOOK));
		}

		public boolean canStart() {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else {
				return !this.mimic.canTarget(livingEntity) ? false : this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
			}
		}

		public void start() {
			this.ticksLeft = toGoalTicks(300);
			super.start();
		}

		public boolean shouldContinue() {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!this.mimic.canTarget(livingEntity)) {
				return false;
			} else {
				return --this.ticksLeft > 0;
			}
		}

		public boolean shouldRunEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingEntity = this.mimic.getTarget();
			if (livingEntity != null) {
				this.mimic.lookAtEntity(livingEntity, 10.0F, 10.0F);
			}

			((PCChestMimic.MimicMoveControl)this.mimic.getMoveControl()).look(this.mimic.getYaw(), this.mimic.canAttack());
		}
	}
	static class MoveGoal extends Goal {
		private final PCChestMimic mimic;

		public MoveGoal(PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
		}

		public boolean canStart() {
			//return !this.mimc.hasVehicle();
			return this.mimic.getTarget() != null;
		}

		public void tick() {
			((PCChestMimic.MimicMoveControl)this.mimic.getMoveControl()).move(moveSpeed);
		}
	}

	static class SwimmingGoal extends Goal {
		private final PCChestMimic mimic;

		public SwimmingGoal(PCChestMimic mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
			mimic.getNavigation().setCanSwim(true);
		}

		public boolean canStart() {
			return (this.mimic.isTouchingWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof PCChestMimic.MimicMoveControl;
		}

		public boolean shouldRunEveryTick() {
			return true;
		}

		public void tick() {
			if (this.mimic.getRandom().nextFloat() < 0.8F) {
				this.mimic.getJumpControl().setActive();
			}

			((PCChestMimic.MimicMoveControl)this.mimic.getMoveControl()).move(4.2D);
		}
	}

	static class IdleGoal extends Goal {
		private final PCChestMimic mimic;

		public IdleGoal(PCChestMimic mimic) {
			this.mimic = mimic;

		}

		public boolean canStart() {
			return !this.mimic.hasVehicle();
		}

		public void tick() {

		}
	}

	float getJumpSoundPitch() {
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	}

	protected SoundEvent getJumpSound() {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
	}

	protected SoundEvent getLandingSound() {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	protected void jump() {
		Vec3d vec3d = this.getVelocity();
		LivingEntity livingEntity = this.getTarget();
		double jumpStrength;
		if (livingEntity == null) {
			jumpStrength = 1D;
		} else {
			jumpStrength = livingEntity.getY() - this.getY();
			jumpStrength = jumpStrength <= 0 ? 1D : jumpStrength / 2.5D + 1.0D;
		}
		//moveSpeed = this.world.random.nextDouble(1.5D,2.1D);
		this.setVelocity(vec3d.x, (double)this.getJumpVelocity()* jumpStrength, vec3d.z);
		this.velocityDirty = true;
		if(this.isGrounded() && this.jumpEndTimer <= 0) {
			this.jumpEndTimer = 10;
			this.setIsJumping(true);
		}
	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height;
	}

	protected boolean canAttack() {
		return true;
	}

	protected float getDamageAmount() {
		return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
	}

	public void onPlayerCollision(PlayerEntity player) {
		if (this.canAttack()) {
			this.damage(player);
		}

	}

	protected void damage(LivingEntity target) {
		if (this.isAlive()) {
			if (this.squaredDistanceTo(target) < 1.5D && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.applyDamageEffects(this, target);
			}
		}
	}

	protected int getTicksUntilNextJump() {
		return this.random.nextInt(40) + 5;
	}
	public void tick() {
		super.tick();
		if(jumpEndTimer >= 0){
			jumpEndTimer -= 1;
		}
		if(this.onGround){
			this.setIsFlying(false);
			this.setIsGrounded(true);
			if(this.onGroundLastTick){
				this.setIsJumping(false);
				if(!this.isSleeping() && !this.isIdle()) {
					timeUntilSleep = 150;
					this.setIsIdle(true);
				}
				if(this.isIdle()){
					timeUntilSleep -= 1;
					if(timeUntilSleep <= 0){
						this.setIsIdle(false);
						this.setIsSleeping(true);
					}
				}
			}else{
				this.playSound(this.getLandingSound(), this.getSoundVolume(),
						((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}
		}else{
			if(spawnWaitTimer > 0){
				spawnWaitTimer -= 1;
			}else{
				this.setIsSleeping(false);
				this.setIsIdle(false);
				this.setIsGrounded(false);
				this.setIsFlying(true);
			}

		}

		this.onGroundLastTick = this.onGround;
	}

	protected boolean isDisallowedInPeaceful() {
		return true;
	}

	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("wasOnGround", this.onGroundLastTick);
		nbt.putBoolean("grounded",this.isOnGround());
		nbt.putBoolean("jumping",this.isJumping());
		nbt.putBoolean("idle",this.isIdle());
		nbt.putBoolean("flying",this.isFlying());
		nbt.putBoolean("sleeping",this.isSleeping());

	}

	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.onGroundLastTick = nbt.getBoolean("wasOnGround");
		this.setIsGrounded(nbt.getBoolean("grounded"));
		this.setIsJumping(nbt.getBoolean("jumping"));
		this.setIsIdle(nbt.getBoolean("idle"));
		this.setIsFlying(nbt.getBoolean("flying"));
		this.setIsSleeping(nbt.getBoolean("sleeping"));
	}

	public void setIsJumping(boolean jumping) {
		this.dataTracker.set(IS_JUMPING, jumping);
	}

	public boolean isJumping() {
		return this.dataTracker.get(IS_JUMPING);
	}

	public void setIsFlying(boolean flying) {
		this.dataTracker.set(IS_FLYING, flying);
	}

	public boolean isFlying() {
		return this.dataTracker.get(IS_FLYING);
	}

	public void setIsGrounded(boolean grounded) {
		this.dataTracker.set(IS_GROUNDED, grounded);
	}

	public boolean isGrounded() {
		return this.dataTracker.get(IS_GROUNDED);
	}

	public void setIsIdle(boolean idle) {
		this.dataTracker.set(IS_IDLE, idle);
	}

	public boolean isIdle() {
		return this.dataTracker.get(IS_IDLE);
	}

	public void setIsSleeping(boolean sleeping) {
		this.dataTracker.set(IS_SLEEPING, sleeping);
	}

	public boolean isSleeping() {
		return this.dataTracker.get(IS_SLEEPING);
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(IS_GROUNDED, true);
		this.dataTracker.startTracking(IS_JUMPING, false);
		this.dataTracker.startTracking(IS_FLYING, false);
		this.dataTracker.startTracking(IS_IDLE, false);
		this.dataTracker.startTracking(IS_SLEEPING, true);
		super.initDataTracker();
	}

}
