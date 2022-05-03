package org.cloudwarp.probablychests.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class PCChestMimicPet extends TameableEntity implements IAnimatable, Tameable, InventoryChangedListener {
	// Animations
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", true)
			.addAnimation("flying", true);
	public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("jump", false);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("close", false).addAnimation("idle", true);
	public static final AnimationBuilder SLEEPING = new AnimationBuilder().addAnimation("sleeping", true);
	public static final AnimationBuilder FLYING = new AnimationBuilder().addAnimation("flying", true);
	private static final String CONTROLLER_NAME = "mimicController";
	// Mimic States
	private static final TrackedData<Boolean> IS_JUMPING;
	private static final TrackedData<Boolean> IS_IDLE;
	private static final TrackedData<Boolean> IS_SLEEPING;
	private static final TrackedData<Boolean> IS_GROUNDED;
	private static final TrackedData<Boolean> IS_FLYING;
	private static final double moveSpeed = 1.0D;

	static {
		IS_JUMPING = DataTracker.registerData(PCChestMimicPet.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_IDLE = DataTracker.registerData(PCChestMimicPet.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_SLEEPING = DataTracker.registerData(PCChestMimicPet.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_GROUNDED = DataTracker.registerData(PCChestMimicPet.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_FLYING = DataTracker.registerData(PCChestMimicPet.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

	private AnimationFactory factory = new AnimationFactory(this);
	private boolean onGroundLastTick;
	private int timeUntilSleep = 0;
	private int jumpEndTimer = 10;
	private boolean isJumpAnimationPlaying = false;
	private boolean isJumpAnimationFinished = false;
	private int spawnWaitTimer = 5;
	public SimpleInventory inventory = new SimpleInventory(54);
	PCChestTypes type;
	public boolean interacting;

	/*
	TODO:
	make not drown
	make fire resistant
	add locking to block pos to be like a normal chest
	fix attacking through shield
	new loot tables
	fix item drop for explosion and set resistance back to 2
	see if changing the pitch to match velocity makes the mimic dive and stuff
	play sound on mimic creation
	custom sounds
	add better forced spread of chest locations
	add minimum position for chests to spawn at in placement modifier
	only 1 pet mimic at a time

	make mimic core 3d cube eye model?
	make my mods ARR
	change icons for mods
	change name in mod credits
	make chests spawn with random rotations
	 */
	public PCChestMimicPet (EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
		this.type = PCChestTypes.NORMAL;
		this.ignoreCameraFrustum = true;
		this.inventory.addListener(this);
		this.moveControl = new PCChestMimicPet.MimicMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
	}

	protected void initGoals () {
		this.goalSelector.add(3, new PCChestMimicPet.SwimmingGoal(this));
		this.goalSelector.add(1, new FollowOwnerGoal(this, 1, 5, 2, false));
		this.goalSelector.add(2, new SitGoal(this));
	}


	public boolean isBreedingItem (ItemStack stack) {
		Item item = stack.getItem();
		return item.isFood() && item.getFoodComponent().isMeat();
	}

	public ActionResult interactMob (PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();

		if (this.isTamed()) {
			if (this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
				if (! player.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				this.heal((float) item.getFoodComponent().getHunger());
				this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
				return ActionResult.SUCCESS;
			}else{
				ActionResult actionResult = super.interactMob(player, hand);
				if(player.isSneaking()) {
					if (this.isOwner(player)) {
						this.setSitting(! this.isSitting());
						this.setIsSleeping(this.isSitting());
						this.jumping = false;
						this.navigation.stop();
						this.setTarget((LivingEntity) null);
						return ActionResult.SUCCESS;
					}

					return actionResult;
				}else{
					if (this.isGrounded() &&  this.canMoveVoluntarily() && !this.world.isClient) {
						this.openGui(player);
					}
					return ActionResult.success(this.world.isClient());
				}
			}
		} else if (itemStack.isOf(PCItems.PET_MIMIC_KEY)) {
			if (! player.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}
			this.setOwner(player);
			this.navigation.stop();
			this.setTarget((LivingEntity) null);
			this.setSitting(true);
			this.setIsSleeping(this.isSitting());
			this.world.sendEntityStatus(this, (byte) 7);

			return ActionResult.SUCCESS;
		}else{
			return super.interactMob(player, hand);
		}
	}

	@Override
	public void onInventoryChanged (Inventory sender) {
	}


	private class MimicScreenHandlerFactory implements NamedScreenHandlerFactory {
		private PCChestMimicPet mimic() {
			return PCChestMimicPet.this;
		}

		@Override
		public Text getDisplayName() {
			return this.mimic().getDisplayName();
		}

		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			var mimicInv = this.mimic().inventory;
			return new PCMimicScreenHandler(type.getScreenHandlerType(), type, syncId, inv, mimicInv);
		}
	}

	public void openGui(PlayerEntity player) {
		if (player.world != null && !this.world.isClient()) {
			this.interacting = true;
			player.openHandledScreen(new MimicScreenHandlerFactory());
		}
	}


	@Override
	protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.getStack(i);
			if (!itemstack.isEmpty()){
				this.dropStack(itemstack);
			}
		}
	}

	@Override
	public int getSafeFallDistance () {
		return 20;
	}

	@Override
	protected int computeFallDamage (float fallDistance, float damageMultiplier) {
		StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
		float f = statusEffectInstance == null ? 0.0F : (float) (statusEffectInstance.getAmplifier() + 1);
		return MathHelper.ceil((fallDistance - 20.0F - f) * damageMultiplier);
	}

	public void printStates () {
		System.out.println(isJumping() + "  " + isFlying() + "  " + isSleeping() + "  " + isGrounded() + "  " + isIdle());
	}

	private <E extends IAnimatable> PlayState devMovement (AnimationEvent<E> animationEvent) {
		//printStates();
		if (isJumping() && ! this.isJumpAnimationFinished) {
			if (! this.isJumpAnimationPlaying) {
				animationEvent.getController().setAnimationSpeed(2D);
				animationEvent.getController().setAnimation(JUMP);
				this.isJumpAnimationPlaying = true;
			} else {
				if (animationEvent.getController().getAnimationState() == AnimationState.Stopped) {
					animationEvent.getController().setAnimationSpeed(1D);
					this.isJumpAnimationPlaying = false;
					this.isJumpAnimationFinished = true;
				}
			}
		} else if (this.isFlying()) {
			animationEvent.getController().setAnimation(FLYING);
		} else if (this.isSleeping()) {
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(SLEEPING);
		} else if (!this.isSleeping()) {
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(IDLE);
		} else if (this.isGrounded()) {
			this.isJumpAnimationFinished = false;
			this.isJumpAnimationPlaying = false;
			animationEvent.getController().setAnimation(CLOSE);
		}
		if (animationEvent.getController().getCurrentAnimation() == null) {
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

	float getJumpSoundPitch () {
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	}

	protected SoundEvent getJumpSound () {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}

	protected SoundEvent getHurtSound (DamageSource source) {
		return SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR;
	}

	protected SoundEvent getDeathSound () {
		return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
	}

	protected SoundEvent getLandingSound () {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	protected void jump () {
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
		this.setVelocity(vec3d.x, (double) this.getJumpVelocity() * jumpStrength, vec3d.z);
		this.velocityDirty = true;
		if (this.isGrounded() && this.jumpEndTimer <= 0) {
			this.jumpEndTimer = 10;
			this.setIsJumping(true);
		}
	}

	protected float getActiveEyeHeight (EntityPose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height;
	}
	/*
	protected boolean canAttack () {
		return true;
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
			if (this.squaredDistanceTo(target) < 1.5D && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.applyDamageEffects(this, target);
			}
		}
	}
	*/
	protected int getTicksUntilNextJump () {
		//return this.random.nextInt(20) + 5;
		return 10;
	}

	public void tick () {
		super.tick();
		if (jumpEndTimer >= 0) {
			jumpEndTimer -= 1;
		}
		if (this.onGround) {
			this.setIsFlying(false);
			this.setIsGrounded(true);
			if (this.onGroundLastTick) {
				this.setIsJumping(false);
			} else {
				this.playSound(this.getLandingSound(), this.getSoundVolume(),
						((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}
		} else {
			if (spawnWaitTimer > 0) {
				spawnWaitTimer -= 1;
			} else {
				this.setIsGrounded(false);
				this.setIsFlying(true);
			}

		}

		this.onGroundLastTick = this.onGround;
	}

	protected boolean isDisallowedInPeaceful () {
		return false;
	}

	public void writeCustomDataToNbt (NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("wasOnGround", this.onGroundLastTick);
		nbt.putBoolean("grounded", this.isOnGround());
		nbt.putBoolean("jumping", this.isJumping());
		nbt.putBoolean("idle", this.isIdle());
		nbt.putBoolean("flying", this.isFlying());
		nbt.putBoolean("sleeping", this.isSleeping());
		NbtList listnbt = new NbtList();
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.getStack(i);
			NbtCompound compoundnbt = new NbtCompound();
			compoundnbt.putByte("Slot", (byte) i);
			itemstack.writeNbt(compoundnbt);
			listnbt.add(compoundnbt);

		}
		nbt.put("Inventory", listnbt);
	}

	public void readCustomDataFromNbt (NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.onGroundLastTick = nbt.getBoolean("wasOnGround");
		this.setIsGrounded(nbt.getBoolean("grounded"));
		this.setIsJumping(nbt.getBoolean("jumping"));
		this.setIsIdle(nbt.getBoolean("idle"));
		this.setIsFlying(nbt.getBoolean("flying"));
		this.setIsSleeping(nbt.getBoolean("sleeping"));
		NbtList listnbt = nbt.getList("Inventory", 10);
		for (int i = 0; i < listnbt.size(); ++i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			this.inventory.setStack(j, ItemStack.fromNbt(compoundnbt));
		}
	}

	public void setIsJumping (boolean jumping) {
		this.dataTracker.set(IS_JUMPING, jumping);
	}

	public boolean isJumping () {
		return this.dataTracker.get(IS_JUMPING);
	}

	public void setIsFlying (boolean flying) {
		this.dataTracker.set(IS_FLYING, flying);
	}

	public boolean isFlying () {
		return this.dataTracker.get(IS_FLYING);
	}

	public void setIsGrounded (boolean grounded) {
		this.dataTracker.set(IS_GROUNDED, grounded);
	}

	public boolean isGrounded () {
		return this.dataTracker.get(IS_GROUNDED);
	}

	public void setIsIdle (boolean idle) {
		this.dataTracker.set(IS_IDLE, idle);
	}

	public boolean isIdle () {
		return this.dataTracker.get(IS_IDLE);
	}

	public void setIsSleeping (boolean sleeping) {
		this.dataTracker.set(IS_SLEEPING, sleeping);
	}

	public boolean isSleeping () {
		return this.dataTracker.get(IS_SLEEPING);
	}

	@Nullable
	@Override
	public PassiveEntity createChild (ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	protected void initDataTracker () {
		this.dataTracker.startTracking(IS_GROUNDED, true);
		this.dataTracker.startTracking(IS_JUMPING, false);
		this.dataTracker.startTracking(IS_FLYING, false);
		this.dataTracker.startTracking(IS_IDLE, false);
		this.dataTracker.startTracking(IS_SLEEPING, true);
		super.initDataTracker();
	}


	private static class MimicMoveControl extends MoveControl {
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
	}

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

	static class FollowOwnerGoal extends Goal {
		private final PCChestMimicPet mimic;
		private final WorldView world;
		private final EntityNavigation navigation;
		private final float maxDistance;
		private final float minDistance;
		private final boolean leavesAllowed;
		private LivingEntity owner;
		private int updateCountdownTicks;
		private float oldWaterPathfindingPenalty;

		public FollowOwnerGoal (PCChestMimicPet mimic, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
			this.mimic = mimic;
			this.world = mimic.world;
			this.navigation = mimic.getNavigation();
			this.minDistance = minDistance;
			this.maxDistance = maxDistance;
			this.leavesAllowed = leavesAllowed;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
			if (! (mimic.getNavigation() instanceof MobNavigation) && ! (mimic.getNavigation() instanceof BirdNavigation)) {
				throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
			}
		}

		public boolean canStart () {
			LivingEntity livingEntity = this.mimic.getOwner();
			if (livingEntity == null) {
				return false;
			} else if (livingEntity.isSpectator()) {
				return false;
			} else if (this.mimic.isSitting()) {
				return false;
			} else if (this.mimic.squaredDistanceTo(livingEntity) < (double) (this.minDistance * this.minDistance)) {
				return false;
			} else {
				this.owner = livingEntity;
				return true;
			}
		}

		public boolean shouldContinue () {
			if (this.navigation.isIdle()) {
				return false;
			} else if (this.mimic.isSitting()) {
				return false;
			} else {
				return ! (this.mimic.squaredDistanceTo(this.owner) <= (double) (this.maxDistance * this.maxDistance));
			}
		}

		public void start () {
			this.updateCountdownTicks = 0;
			this.oldWaterPathfindingPenalty = this.mimic.getPathfindingPenalty(PathNodeType.WATER);
			this.mimic.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
			this.mimic.setTarget(this.owner);
		}

		public void stop () {
			this.owner = null;
			this.navigation.stop();
			this.mimic.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
		}

		public void tick () {
			if (this.owner != null) {
				this.mimic.lookAtEntity(this.owner, 40.0F, 40.0F);
			}
			((PCChestMimicPet.MimicMoveControl) this.mimic.getMoveControl()).look(this.mimic.getYaw(), true);
			this.mimic.getLookControl().lookAt(this.owner, 40.0F, (float) this.mimic.getMaxLookPitchChange());
			if (-- this.updateCountdownTicks <= 0) {
				this.updateCountdownTicks = this.getTickCount(10);
				if (! this.mimic.isLeashed() && ! this.mimic.hasVehicle()) {
					if (this.mimic.squaredDistanceTo(this.owner) >= 184.0D) {
						this.tryTeleport();
					}else{
						((PCChestMimicPet.MimicMoveControl) this.mimic.getMoveControl()).move(moveSpeed);
					}
				}
			}
		}

		private void tryTeleport () {
			BlockPos blockPos = this.owner.getBlockPos();

			for (int i = 0; i < 10; ++ i) {
				int j = this.getRandomInt(- 3, 3);
				int k = this.getRandomInt(- 1, 1);
				int l = this.getRandomInt(- 3, 3);
				boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
				if (bl) {
					return;
				}
			}

		}

		private boolean tryTeleportTo (int x, int y, int z) {
			if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
				return false;
			} else if (! this.canTeleportTo(new BlockPos(x, y, z))) {
				return false;
			} else {
				this.mimic.refreshPositionAndAngles((double) x + 0.5D, (double) y, (double) z + 0.5D, this.mimic.getYaw(), this.mimic.getPitch());
				this.navigation.stop();
				return true;
			}
		}

		private boolean canTeleportTo (BlockPos pos) {
			PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
			if (pathNodeType != PathNodeType.WALKABLE) {
				return false;
			} else {
				BlockState blockState = this.world.getBlockState(pos.down());
				if (! this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
					return false;
				} else {
					BlockPos blockPos = pos.subtract(this.mimic.getBlockPos());
					return this.world.isSpaceEmpty(this.mimic, this.mimic.getBoundingBox().offset(blockPos));
				}
			}
		}

		private int getRandomInt (int min, int max) {
			return this.mimic.getRandom().nextInt(max - min + 1) + min;
		}
	}


	static class SwimmingGoal extends Goal {
		private final PCChestMimicPet mimic;

		public SwimmingGoal (PCChestMimicPet mimic) {
			this.mimic = mimic;
			this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
			mimic.getNavigation().setCanSwim(true);
		}

		public boolean canStart () {
			return (this.mimic.isTouchingWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof PCChestMimicPet.MimicMoveControl;
		}

		public boolean shouldRunEveryTick () {
			return true;
		}

		public void tick () {
			if (this.mimic.getRandom().nextFloat() < 0.8F) {
				this.mimic.getJumpControl().setActive();
			}

			((PCChestMimicPet.MimicMoveControl) this.mimic.getMoveControl()).move(4.2D);
		}
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

}
