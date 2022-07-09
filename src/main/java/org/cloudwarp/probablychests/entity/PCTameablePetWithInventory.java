package org.cloudwarp.probablychests.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.jetbrains.annotations.Nullable;

public abstract class PCTameablePetWithInventory extends TameableEntity implements Tameable, InventoryChangedListener {

	public SimpleInventory inventory = new SimpleInventory(54);
	public boolean interacting;
	PCChestTypes type;

	private static final TrackedData<Integer> MIMIC_STATE;
	public Integer previousState = 0;

	static {
		MIMIC_STATE = DataTracker.registerData(PCChestMimic.class, TrackedDataHandlerRegistry.INTEGER);
	}

	// Mimic States
	public static final int IS_SLEEPING = 0;
	public static final int IS_IN_AIR = 1;
	public static final int IS_CLOSED = 2;
	public static final int IS_IDLE = 3;
	public static final int IS_JUMPING = 4;
	public static final int IS_OPENED = 5;
	public static final int IS_STANDING = 6;
	public static final int IS_SITTING = 7;
	public static final int IS_OPENING = 8;


	public int viewerCount = 0;

	public int closeAnimationTimer = 12;
	public int openAnimationTimer = 12;


	public PCTameablePetWithInventory (EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
		this.type = PCChestTypes.NORMAL;
		this.ignoreCameraFrustum = true;
		this.inventory.addListener(this);
	}

	public static DefaultAttributeContainer.Builder createMobAttributes () {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D);
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
			} else {
				ActionResult actionResult = super.interactMob(player, hand);
				if (player.isSneaking()) {
					if (this.isOwner(player)) {
						this.setSitting(! this.isSitting());
						this.updateSitting(player);
						this.playSound(this.isSitting() ? this.getSitSound() : this.getStandSound(), this.getSoundVolume(), 0.9F);
						this.jumping = false;
						this.navigation.stop();
						this.setTarget((LivingEntity) null);
						return ActionResult.SUCCESS;
					}

					return actionResult;
				} else {
					if (this.canMoveVoluntarily() && ! this.world.isClient) {
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
			this.updateSitting(player);
			this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F);
			this.world.sendEntityStatus(this, (byte) 7);

			return ActionResult.SUCCESS;
		} else {
			return super.interactMob(player, hand);
		}
	}
	public void updateSitting(PlayerEntity player) {}

	@Override
	protected float getSoundVolume () {
		return 0.6F;
	}

	@Override
	public void onInventoryChanged (Inventory sender) {
	}

	public TrackedData<Integer> getMimicStateVariable(){
		return this.MIMIC_STATE;
	}

	public void openGui (PlayerEntity player) {
		if (player.world != null && ! this.world.isClient()) {
			((PlayerEntityAccess)player).openMimicInventory(this,this.inventory);
			if(this.viewerCount == 0){
				openAnimationTimer = 12;
				this.setMimicState(IS_OPENING);
				this.playSound(this.getOpenSound(),this.getSoundVolume(),0.8F);
			}
			this.viewerCount += 1;
		}
	}
	public void closeGui (PlayerEntity player) {
		if (player.world != null && ! this.world.isClient()) {
			viewerCount -= 1;
			if(viewerCount == 0){
				closeAnimationTimer = 12;
				this.setMimicState(IS_CLOSED);
				this.playSound(this.getCloseSound(), this.getSoundVolume(), 0.8F);
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
	}

	public void setMimicState (int state) {
		this.dataTracker.set(MIMIC_STATE, state);
	}

	public int getMimicState () {
		return this.dataTracker.get(MIMIC_STATE);
	}

	@Nullable
	@Override
	public PassiveEntity createChild (ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	protected void initDataTracker () {
		super.initDataTracker();
	}

	@Override
	public boolean canBreatheInWater () {
		return true;
	}

	@Override
	public boolean canFreeze () {
		return false;
	}

	public boolean areInventoriesDifferent(Inventory other) {
		return this.inventory != other;
	}

}
