package org.cloudwarp.probablychests.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.entity.ai.MimicMoveControl;
import org.cloudwarp.probablychests.entity.ai.PCMeleeAttackGoal;
import org.cloudwarp.probablychests.entity.ai.PCMimicEscapeDangerGoal;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


import java.util.EnumSet;

public class PCChestMimicPet extends PCTameablePetWithInventory implements GeoAnimatable, Tameable {
	// Animations
	public static final RawAnimation IDLE = RawAnimation.begin().then("idle", Animation.LoopType.LOOP);
	public static final RawAnimation JUMP = RawAnimation.begin().then("jump", Animation.LoopType.PLAY_ONCE).then("flying", Animation.LoopType.LOOP);
	public static final RawAnimation CLOSE_SITTING = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE).then("sleeping", Animation.LoopType.LOOP);
	public static final RawAnimation CLOSE_STANDING = RawAnimation.begin().then("close", Animation.LoopType.PLAY_ONCE).then("standing", Animation.LoopType.LOOP);
	public static final RawAnimation OPENING = RawAnimation.begin().then("open", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation OPENED = RawAnimation.begin().then("opened", Animation.LoopType.LOOP);
	public static final RawAnimation SITTING = RawAnimation.begin().then("sleeping", Animation.LoopType.LOOP);
	public static final RawAnimation STANDING = RawAnimation.begin().then("standing", Animation.LoopType.LOOP);
	public static final RawAnimation FLYING = RawAnimation.begin().then("flying", Animation.LoopType.LOOP);
	public static final RawAnimation BITING = RawAnimation.begin().then("bite", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation LOW_WAG = RawAnimation.begin().then("lowWag", Animation.LoopType.LOOP);
	public static final RawAnimation FLYING_WAG = RawAnimation.begin().then("flyingWag", Animation.LoopType.LOOP);
	public static final RawAnimation IDLE_WAG = RawAnimation.begin().then("idleWag", Animation.LoopType.LOOP);
	public static final RawAnimation NO_WAG = RawAnimation.begin().then("noWag", Animation.LoopType.LOOP);
	private static final String MIMIC_CONTROLLER = "mimicController";
	private static final String TONGUE_CONTROLLER = "tongueController";





	public SimpleInventory inventory = new SimpleInventory(54);
	PCChestTypes type;
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean onGroundLastTick;
	private int jumpEndTimer = 10;
	private int spawnWaitTimer = 5;


	public PCChestMimicPet (EntityType<? extends PCTameablePetWithInventory> entityType, World world) {
		super(entityType, world);
		this.type = PCChestTypes.NORMAL;
		this.ignoreCameraFrustum = true;
		this.inventory.addListener(this);
		this.moveControl = new MimicMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
	}

	protected void initGoals () {
		this.goalSelector.add(1, new PCTameablePetWithInventory.SwimmingGoal(this));
		this.goalSelector.add(2, new SitGoal(this));
		//this.goalSelector.add(1, new PetMimicEscapeDangerGoal(1.5));
		this.goalSelector.add(5, new PCMeleeAttackGoal(this, 1.0, true));
		this.goalSelector.add(6, new FollowOwnerGoal(this, 1, 5, 2, false));
		this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		this.targetSelector.add(3, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new AttackWithOwnerGoal(this));
		this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
		this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
	}

	@Override
	public EntityView method_48926 () {
		return null;
	}

	class PetMimicEscapeDangerGoal
			extends PCMimicEscapeDangerGoal {
		public PetMimicEscapeDangerGoal (double speed) {
			super(PCChestMimicPet.this, speed);
		}

		@Override
		protected boolean isInDanger () {
			return this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
		}
	}


	public boolean isBreedingItem (ItemStack stack) {
		Item item = stack.getItem();
		return item.isFood() && item.getFoodComponent().isMeat();
	}

	public ActionResult interactMob (PlayerEntity player, Hand hand) {
		return super.interactMob(player, hand);
	}


	private <E extends GeoAnimatable> PlayState chestMovement (AnimationState<E> eAnimationState) {
		int state = this.getMimicState();
		eAnimationState.getController().setAnimationSpeed(1D);
		eAnimationState.getController().setTransitionLength(6);
		eAnimationState.getController().setOverrideEasingType(EasingType.EASE_IN_OUT_SINE);
		if (state == IS_IN_AIR) {
			eAnimationState.getController().setTransitionLength(2);
			eAnimationState.getController().setAnimation(FLYING);
		} else if (state == IS_IDLE) {
			if (this.getIsOpenState()) {
				eAnimationState.getController().setAnimation(OPENED);
			} else {
				if(this.isInSittingPose()) {
					eAnimationState.getController().setAnimation(SITTING);
				}else{
					eAnimationState.getController().setAnimation(STANDING);
				}
			}
		} else if (state == IS_JUMPING) {
			eAnimationState.getController().setAnimationSpeed(2D);
			eAnimationState.getController().setAnimation(JUMP);
		} else if (state == IS_BITING) {
			eAnimationState.getController().setTransitionLength(2);
			eAnimationState.getController().setAnimationSpeed(1.5D);
			eAnimationState.getController().setAnimation(BITING);
		} else {
			System.out.println("INVALID STATE: " + state);
		}
		return PlayState.CONTINUE;
	}
	private <E extends GeoAnimatable> PlayState tongueMovement (AnimationState<E> eAnimationState) {
		int state = this.getMimicState();
		eAnimationState.getController().setAnimationSpeed(1D);
		eAnimationState.getController().setTransitionLength(6);
		if (state == IS_IN_AIR) {
			eAnimationState.getController().setTransitionLength(2);
			eAnimationState.getController().setAnimation(FLYING_WAG);
		} else if (state == IS_IDLE) {
			if (this.getIsOpenState()) {
				eAnimationState.getController().setAnimation(IDLE_WAG);
			} else {
				if(this.isInSittingPose()) {
					eAnimationState.getController().setAnimation(NO_WAG);
				}else{
					eAnimationState.getController().setAnimation(LOW_WAG);
				}
			}
		} else if (state == IS_JUMPING) {
			eAnimationState.getController().setAnimationSpeed(2D);
			eAnimationState.getController().setAnimation(FLYING_WAG);
		} else if (state == IS_BITING) {
		} else {
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>(this, MIMIC_CONTROLLER, 6, this::chestMovement));
		controllerRegistrar.add(new AnimationController<>(this, TONGUE_CONTROLLER, 6, this::tongueMovement));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache () {
		return cache;
	}

	@Override
	public double getTick (Object o) {
		return 0;
	}


	protected void jump () {
		Vec3d vec3d = this.getVelocity();
		LivingEntity livingEntity = this.getTarget();
		double jumpStrength;
		if (livingEntity == null) {
			jumpStrength = 1D;
		} else {
			jumpStrength = livingEntity.getY() - this.getY();
			jumpStrength = jumpStrength <= 0 ? 1D : jumpStrength / 3.5D + 1.0D;
		}
		//moveSpeed = this.world.random.nextDouble(1.5D,2.1D);
		this.setVelocity(vec3d.x, (double) this.getJumpVelocity() * jumpStrength, vec3d.z);
		this.velocityDirty = true;
		if (this.isOnGround() && this.jumpEndTimer <= 0) {
			this.jumpEndTimer = 10;
			this.setMimicState(IS_JUMPING);
		}
	}


	public int getTicksUntilNextJump () {
		return 10;
	}

	public void tickMovement () {
		super.tickMovement();
		if (! this.world.isClient) {
			this.tickAngerLogic((ServerWorld) this.world, true);
		}
	}

	public void tick () {
		super.tick();
		if (this.world.isClient()) {
			return;
		}
		if (jumpEndTimer >= 0) {
			jumpEndTimer -= 1;
		}
		if (biteAnimationTimer > 0) {
			biteAnimationTimer -= 1;
		}
		if (this.onGround) {
			if (! this.onGroundLastTick) {
				this.playSound(this.getLandingSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}
			if (biteAnimationTimer <= 0) {
				this.setMimicState(IS_IDLE);
			}
		} else {
			if (this.getMimicState() != IS_JUMPING) {
				if (spawnWaitTimer > 0) {
					spawnWaitTimer -= 1;
				} else {
					this.setMimicState(IS_IN_AIR);
				}
			}
		}
		this.onGroundLastTick = this.onGround;
		if (this.getIsAbandoned()) {
			this.setMimicState(IS_IDLE);
		}
	}

	protected boolean isDisallowedInPeaceful () {
		return false;
	}

	public void writeCustomDataToNbt (NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("wasOnGround", this.onGroundLastTick);
	}

	public void readCustomDataFromNbt (NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.onGroundLastTick = nbt.getBoolean("wasOnGround");
	}


	@Nullable
	@Override
	public PassiveEntity createChild (ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	protected void initDataTracker () {
		this.dataTracker.startTracking(getMimicStateVariable(), IS_IDLE);
		super.initDataTracker();
	}

	@Override
	public boolean canBeLeashedBy (PlayerEntity player) {
		return false;
	}

	/*public static class MimicMoveControl extends MoveControl {
		private final PCChestMimicPet mimic;
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public MimicMoveControl (PCChestMimicPet mimic) {
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
	}*/

	static class SitGoal extends Goal {
		private final PCChestMimicPet mimic;

		public SitGoal (PCChestMimicPet mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
		}

		public boolean shouldContinue () {
			return this.mimic.isSitting();
		}

		public boolean canStart () {
			if (! this.mimic.isTamed()) {
				return false;
			} else if (this.mimic.isInsideWaterOrBubbleColumn()) {
				return false;
			} else if (! this.mimic.isOnGround()) {
				return false;
			} else {
				LivingEntity livingEntity = this.mimic.getOwner();
				if (livingEntity == null) {
					return true;
				} else {
					return this.mimic.squaredDistanceTo(livingEntity) < 144.0D && livingEntity.getAttacker() != null ? false : this.mimic.isSitting();
				}
			}
		}

		public void start () {
			this.mimic.getNavigation().stop();
			this.mimic.setInSittingPose(true);
		}

		public void stop () {
			this.mimic.setInSittingPose(false);
		}
	}

	public boolean tryAttack (Entity target) {
		boolean bl = target.damage(this.getDamageSources().mobAttack(this), (float) ((int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
		if (bl) {
			this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
			this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
			this.applyDamageEffects(this, target);
		}

		return bl;
	}


	public boolean canAttackWithOwner (LivingEntity target, LivingEntity owner) {
		if (! (target instanceof GhastEntity) || this.getIsAbandoned()) {
			if (target instanceof PCChestMimicPet) {
				PCChestMimicPet mimic = (PCChestMimicPet) target;
				return ! mimic.isTamed() || mimic.getOwner() != owner;
			} else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && ! ((PlayerEntity) owner).shouldDamagePlayer((PlayerEntity) target)) {
				return false;
			} else {
				return ! (target instanceof TameableEntity) || ! ((TameableEntity) target).isTamed();
			}
		} else {
			return false;
		}
	}

	public boolean damage (DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			Entity entity = source.getAttacker();
			if (! this.world.isClient) {
				this.setSitting(false);
				this.setMimicState(IS_IDLE);
			}

			if (entity != null && ! (entity instanceof PlayerEntity) && ! (entity instanceof PersistentProjectileEntity)) {
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.damage(source, amount);
		}
	}

}
