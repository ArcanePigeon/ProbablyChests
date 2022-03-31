package org.cloudwarp.probablychests.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PCChestBlockEntity extends LootableContainerBlockEntity implements IAnimatable {

	private final AnimationFactory factory = new AnimationFactory(this);
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("animation.PCChestBlock.idle", false);
	public static final AnimationBuilder OPEN = new AnimationBuilder().addAnimation("animation.PCChestBlock.open", false);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("animation.PCChestBlock.close", false);

	PCChestTypes type;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);
	private final ViewerCountManager stateManager = new ViewerCountManager(){

		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
			PCChestBlockEntity.this.playSound(state, SoundEvents.BLOCK_CHEST_OPEN);
		}

		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state) {
			PCChestBlockEntity.this.playSound(state, SoundEvents.BLOCK_CHEST_CLOSE);
		}

		@Override
		protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
			PCChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		}

		@Override
		protected boolean isPlayerViewing(PlayerEntity player) {
			if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
				Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
				return inventory == PCChestBlockEntity.this || inventory instanceof DoubleInventory && ((DoubleInventory)inventory).isPart(PCChestBlockEntity.this);
			}
			return false;
		}
	};

	public PCChestBlockEntity(PCChestTypes type, BlockPos  pos, BlockState state) {
		super(type.getBlockEntityType(), pos, state);
		this.type = type;
		this.setInvStackList(DefaultedList.ofSize(this.size(), ItemStack.EMPTY));
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
	}

	@Override
	public void onOpen(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	public static int getPlayersLookingInChestCount(BlockView world, BlockPos pos) {
		BlockEntity blockEntity;
		BlockState blockState = world.getBlockState(pos);
		if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(pos)) instanceof PCChestBlockEntity) {
			return ((PCChestBlockEntity)blockEntity).stateManager.getViewerCount();
		}
		return 0;
	}

	public static void copyInventory(PCChestBlockEntity from, PCChestBlockEntity to) {
		DefaultedList<ItemStack> defaultedList = from.getInvStackList();
		from.setInvStackList(to.getInvStackList());
		to.setInvStackList(defaultedList);
	}

	public void onScheduledTick() {
		if (!this.removed) {
			this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		Block block = state.getBlock();
		world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
	}


	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			return true;
		}
		return super.onSyncedBlockEvent(type, data);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 2, animationEvent -> {
			AnimationBuilder anime = new AnimationBuilder().clearAnimations();
			animationEvent.getController().getAnimationState();
			anime = CLOSE;
			if(isOpen()) {
				animationEvent.getController().setAnimation(OPEN);
			} else {
				animationEvent.getController().setAnimation(anime);
			}
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
		return new PCScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
		return new PCScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	public int size() {
		return type.size;
	}

	public PCChestTypes type() {
		return type;
	}

	private void playSound(BlockState state, SoundEvent soundEvent) {
		double d = (double) this.pos.getX() + 0.5D;
		double e = (double) this.pos.getY() + 0.5D;
		double f = (double) this.pos.getZ() + 0.5D;
		this.world.playSound(d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F, false);
	}
}
