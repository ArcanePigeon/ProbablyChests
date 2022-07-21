package org.cloudwarp.probablychests.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCEventHandler;
import org.jetbrains.annotations.Nullable;

import static org.cloudwarp.probablychests.utils.PCMimicCreationUtils.*;

import java.util.UUID;

public abstract class PCTameablePetWithInventory extends TameableEntity implements Tameable, InventoryChangedListener, Angerable {

	public SimpleInventory inventory = new SimpleInventory(54);
	public boolean interacting;
	PCChestTypes type;
	@Nullable
	private UUID angryAt;

	private static final TrackedData<Integer> MIMIC_STATE;
	private static final TrackedData<Integer> ANGER_TIME;
	private static final UniformIntProvider ANGER_TIME_RANGE;
	private static final TrackedData<Boolean> IS_ABANDONED;
	private static final TrackedData<Boolean> MIMIC_HAS_LOCK;
	private static final TrackedData<Boolean> IS_MIMIC_LOCKED;
	private static final TrackedData<Boolean> IS_OPEN_STATE;


	static {
		MIMIC_STATE = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.INTEGER);
		ANGER_TIME = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.INTEGER);
		IS_ABANDONED = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.BOOLEAN);
		MIMIC_HAS_LOCK = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_MIMIC_LOCKED = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.BOOLEAN);
		IS_OPEN_STATE = DataTracker.registerData(PCTameablePetWithInventory.class, TrackedDataHandlerRegistry.BOOLEAN);
		ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
	}

	// Mimic States
	public static final int IS_SLEEPING = 0;
	public static final int IS_IN_AIR = 1;
	public static final int IS_IDLE = 2;
	public static final int IS_JUMPING = 3;
	public static final int IS_BITING = 4;
	public static final int IS_LANDING = 5;



	public int viewerCount = 0;

	public int closeAnimationTimer;
	public int openAnimationTimer;
	public int biteAnimationTimer;
	public int isAbandonedTimer;
	public int biteDamageAmount = 2;


	public PCTameablePetWithInventory (EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
		this.type = PCChestTypes.NORMAL;
		this.ignoreCameraFrustum = true;
		this.inventory.addListener(this);
		this.isAbandonedTimer = ProbablyChests.loadedConfig.mimicSettings.abandonedMimicTimer * 1200;
	}

	public void setType (PCChestTypes type) {
		this.type = type;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
	}


	@Override
	public void remove (RemovalReason reason) {
		super.remove(reason);
		if (this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity) {
			((PlayerEntityAccess) this.getOwner()).removePetMimicFromOwnedList(this.getUuid());
		}
	}

	public void tick () {
		super.tick();
		if (this.getOwner() != null && ! this.world.isClient() && this.isTamed()) {
			if (this.getIsAbandoned()) {
				if (this.isAbandonedTimer > 0) {
					this.isAbandonedTimer--;
				} else {
					this.setTamed(false);

					convertPetMimicToHostile(world, type, this);
				}
			}
		}
	}

	public boolean isBreedingItem (ItemStack stack) {
		Item item = stack.getItem();
		return item.isFood() && item.getFoodComponent().isMeat();
	}


	public ActionResult interactMob (PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		if (this.world.isClient()) {
			return ActionResult.SUCCESS;
		}
		PCConfig config = ProbablyChests.loadedConfig;

		if (this.isTamed()) {
			if (! this.getIsAbandoned() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
				if (! player.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				this.heal((float) item.getFoodComponent().getHunger());
				this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
				this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
				return ActionResult.SUCCESS;
			} else if (itemStack.isOf(PCItems.MIMIC_HAND_BELL)) {
				if (! this.getIsAbandoned() && this.getOwner() == player) {
					if (player.isSneaking()) {
						((PlayerEntityAccess) player).removeMimicFromKeepList(this.getUuid());
						// 4 8
						this.playSound(PCSounds.BELL_HIT_4, this.getSoundVolume(), 0.5F + getPitchOffset(0.1F));
					} else {
						((PlayerEntityAccess) player).addMimicToKeepList(this.getUuid());
						this.playSound(PCSounds.BELL_HIT_4, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
					}
				} else {
					if (this.getIsAbandoned() && !((PlayerEntityAccess)player).checkForMimicLimit()) {
						this.setOwner(player);
						this.setIsAbandoned(false);
						this.isAbandonedTimer = ProbablyChests.loadedConfig.mimicSettings.abandonedMimicTimer * 1200;
						((PlayerEntityAccess) player).addPetMimicToOwnedList(this.getUuid());
						this.playSound(PCSounds.BELL_HIT_2, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
					}
				}
				return ActionResult.SUCCESS;
			} else if (this.getOwner() == player && ! this.getIsAbandoned() && itemStack.isOf(PCItems.IRON_LOCK) && ! this.getMimicHasLock() && config.mimicSettings.allowPetMimicLocking) {
				this.setMimicHasLock(true);
				this.setIsMimicLocked(true);
				if (! player.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				this.playSound(PCSounds.APPLY_LOCK1, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
			} else if (this.getOwner() == player && ! this.getIsAbandoned() && itemStack.isOf(PCItems.IRON_KEY) && this.getMimicHasLock() && config.mimicSettings.allowPetMimicLocking) {
				this.setIsMimicLocked(! this.getIsMimicLocked());
				if (this.getIsMimicLocked()) {
					this.playSound(PCSounds.LOCK_UNLOCK, this.getSoundVolume(), 1.3F + getPitchOffset(0.1F));
				} else {
					this.playSound(PCSounds.LOCK_UNLOCK, this.getSoundVolume(), 0.6F + getPitchOffset(0.1F));
				}
			} else if (! this.getIsAbandoned()) {
				ActionResult actionResult = super.interactMob(player, hand);
				if (player.isSneaking()) {
					if (this.isOwner(player)) {
						this.setSitting(! this.isSitting());
						this.updateSitting(player);
						this.playSound(this.isSitting() ? this.getSitSound() : this.getStandSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
						this.jumping = false;
						this.navigation.stop();
						this.setTarget((LivingEntity) null);
						return ActionResult.SUCCESS;
					}

					return actionResult;
				} else {
					if (this.canMoveVoluntarily()) {
						if (this.getIsMimicLocked() && config.mimicSettings.allowPetMimicLocking) {
							if (this.getOwner() == player) {
								this.openGui(player);
							} else {
								this.bite(player);
								this.biteAnimationTimer = 6;
								this.setMimicState(IS_BITING);
							}
						} else {
							this.openGui(player);
						}
					}
					return ActionResult.success(this.world.isClient());
				}
			}
		} else if (itemStack.isOf(PCItems.PET_MIMIC_KEY)) {
			if (! player.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}
			if(this instanceof PCChestMimicPet) {
				this.setOwner(player);
				this.navigation.stop();
				this.setTarget((LivingEntity) null);
				this.setSitting(true);
				this.updateSitting(player);
				this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
				this.world.sendEntityStatus(this, (byte) 7);
			}else{
				convertHostileMimicToPet(world, type, this, player);
				this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + getPitchOffset(0.1F));
			}

			return ActionResult.SUCCESS;
		} else {
			return super.interactMob(player, hand);
		}
		return ActionResult.FAIL;
	}

	public void bite (LivingEntity target) {
		if (this.isAlive()) {
			if (target.damage(DamageSource.mob(this), this.biteDamageAmount)) {
				this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
				this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + getPitchOffset(0.2F));
				this.applyDamageEffects(this, target);
			}
		}
	}

	public float getPitchOffset (float range) {
		return (this.random.nextFloat() - this.random.nextFloat()) * range;
	}


	public void updateSitting (PlayerEntity player) {
	}

	@Override
	protected float getSoundVolume () {
		return 0.6F;
	}

	@Override
	public void onInventoryChanged (Inventory sender) {
	}

	public TrackedData<Integer> getMimicStateVariable () {
		return this.MIMIC_STATE;
	}

	public void openGui (PlayerEntity player) {
		if (player.world != null && ! this.world.isClient()) {
			if (this.viewerCount == 0) {
				openAnimationTimer = 12;
				this.setIsOpenState(true);
				this.playSound(this.getOpenSound(), this.getSoundVolume(), 0.8F + getPitchOffset(0.1F));
				this.playSound(PCSounds.CLOSE_2, this.getSoundVolume(), 1.5F + getPitchOffset(0.1F));
			}
			this.viewerCount++;
			int syncId = player.openHandledScreen(new MimicScreenHandlerFactory()).getAsInt();
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(this.inventory.size());
			buf.writeVarInt(this.getId());
			buf.writeByte(syncId);
			for (ServerPlayerEntity p : PlayerLookup.tracking(this)) {
				ServerPlayNetworking.send(p, PCEventHandler.MIMIC_INVENTORY_PACKET_ID, buf);
			}
		}
	}

	public void closeGui (PlayerEntity player) {
		if (player.world != null && ! this.world.isClient()) {
			viewerCount -= 1;
			if (viewerCount == 0) {
				closeAnimationTimer = 12;
				this.setIsOpenState(false);
				this.playSound(this.getCloseSound(), this.getSoundVolume(), 0.8F + getPitchOffset(0.1F));
				this.playSound(PCSounds.CLOSE_2, this.getSoundVolume(), 1.0F + getPitchOffset(0.1F));
			}
			if (viewerCount < 0) {
				System.out.println("this should not happen but i added a check just in case. Viewer count of pet mimic is less than 0");
				viewerCount = 0;
			}
		}
	}

	@Override
	protected void dropEquipment (DamageSource source, int lootingMultiplier, boolean allowDrops) {
		for (int i = 0; i < this.inventory.size(); ++ i) {
			ItemStack itemstack = this.inventory.getStack(i);
			if (! itemstack.isEmpty()) {
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


	float getJumpSoundPitch () {
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F);
	}

	protected SoundEvent getJumpSound () {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}

	protected SoundEvent getSitSound () {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	protected SoundEvent getStandSound () {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}

	protected SoundEvent getCloseSound () {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	protected SoundEvent getOpenSound () {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}

	protected SoundEvent getHurtSound () {
		return SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR;
	}

	protected SoundEvent getDeathSound () {
		return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
	}

	protected SoundEvent getLandingSound () {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}

	static void playSound (World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.5;
		double f = (double) pos.getZ() + 0.5;

		world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
	}


	protected float getActiveEyeHeight (EntityPose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height;
	}


	protected boolean isDisallowedInPeaceful () {
		return false;
	}

	public void writeCustomDataToNbt (NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("state", this.getMimicState());
		NbtList listnbt = new NbtList();
		for (int i = 0; i < this.inventory.size(); ++ i) {
			ItemStack itemstack = this.inventory.getStack(i);
			NbtCompound compoundnbt = new NbtCompound();
			compoundnbt.putByte("Slot", (byte) i);
			itemstack.writeNbt(compoundnbt);
			listnbt.add(compoundnbt);

		}
		nbt.put("Inventory", listnbt);
		nbt.putInt("mimic_state",this.getMimicState());
		nbt.putBoolean("is_abandoned", this.getIsAbandoned());
		nbt.putBoolean("mimic_has_lock", this.getMimicHasLock());
		nbt.putBoolean("is_mimic_locked", this.getIsMimicLocked());
		this.writeAngerToNbt(nbt);
	}

	public void readCustomDataFromNbt (NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setMimicState(nbt.getInt("state"));
		NbtList listnbt = nbt.getList("Inventory", 10);
		for (int i = 0; i < listnbt.size(); ++ i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			this.inventory.setStack(j, ItemStack.fromNbt(compoundnbt));
		}
		this.readAngerFromNbt(this.world, nbt);
		this.setMimicState(nbt.getInt("mimic_state"));
		this.setIsAbandoned(nbt.getBoolean("is_abandoned"));
		this.setMimicHasLock(nbt.getBoolean("mimic_has_lock"));
		this.setIsMimicLocked(nbt.getBoolean("is_mimic_locked"));
	}

	public void setMimicState (int state) {
		this.dataTracker.set(MIMIC_STATE, state);
	}

	public int getMimicState () {
		return this.dataTracker.get(MIMIC_STATE);
	}

	public void setIsOpenState (boolean state) {
		this.dataTracker.set(IS_OPEN_STATE, state);
	}

	public boolean getIsOpenState () {
		return this.dataTracker.get(IS_OPEN_STATE);
	}

	public void setIsAbandoned (boolean state) {
		this.dataTracker.set(IS_ABANDONED, state);
	}

	public boolean getIsAbandoned () {
		return this.dataTracker.get(IS_ABANDONED);
	}

	public void setMimicHasLock (boolean state) {
		this.dataTracker.set(MIMIC_HAS_LOCK, state);
	}

	public boolean getMimicHasLock () {
		return this.dataTracker.get(MIMIC_HAS_LOCK);
	}

	public void setIsMimicLocked (boolean state) {
		this.dataTracker.set(IS_MIMIC_LOCKED, state);
	}

	public boolean getIsMimicLocked () {
		return this.dataTracker.get(IS_MIMIC_LOCKED);
	}

	@Nullable
	@Override
	public PassiveEntity createChild (ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	protected void initDataTracker () {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER_TIME, 0);
		this.dataTracker.startTracking(IS_ABANDONED, false);
		this.dataTracker.startTracking(MIMIC_HAS_LOCK, false);
		this.dataTracker.startTracking(IS_MIMIC_LOCKED, false);
		this.dataTracker.startTracking(IS_OPEN_STATE, false);
	}

	@Override
	public boolean canBreatheInWater () {
		return true;
	}

	@Override
	public boolean canFreeze () {
		return false;
	}

	public boolean areInventoriesDifferent (Inventory other) {
		return this.inventory != other;
	}

	public int getAngerTime () {
		return (Integer) this.dataTracker.get(ANGER_TIME);
	}

	public void setAngerTime (int angerTime) {
		this.dataTracker.set(ANGER_TIME, angerTime);
	}

	public void chooseRandomAngerTime () {
		this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
	}

	@Nullable
	public UUID getAngryAt () {
		return this.angryAt;
	}

	public void setAngryAt (@Nullable UUID angryAt) {
		this.angryAt = angryAt;
	}

	private class MimicScreenHandlerFactory implements NamedScreenHandlerFactory {
		private PCTameablePetWithInventory mimic () {
			return PCTameablePetWithInventory.this;
		}

		@Override
		public Text getDisplayName () {
			return this.mimic().getDisplayName();
		}

		@Override
		public ScreenHandler createMenu (int syncId, PlayerInventory inv, PlayerEntity player) {
			var mimicInv = this.mimic().inventory;
			PCMimicScreenHandler screenHandler = PCMimicScreenHandler.createScreenHandler(syncId, inv, mimicInv);
			screenHandler.setMimicEntity(this.mimic());
			return screenHandler;
		}
	}

}
