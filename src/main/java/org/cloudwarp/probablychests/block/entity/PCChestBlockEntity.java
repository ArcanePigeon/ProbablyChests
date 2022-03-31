package org.cloudwarp.probablychests.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PCChestBlockEntity extends LootableContainerBlockEntity implements IAnimatable, ISyncable {

	private final AnimationFactory factory = new AnimationFactory(this);
	private static final String CONTROLLER_NAME = "chestController";
	private static final int ANIM_IDLE = 0;
	private static final int ANIM_OPEN = 1;
	private static final int ANIM_CLOSE = 2;
	public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("animation.PCChestBlock.idle", false);
	public static final AnimationBuilder OPEN = new AnimationBuilder().addAnimation("animation.PCChestBlock.open", false);
	public static final AnimationBuilder CLOSE = new AnimationBuilder().addAnimation("animation.PCChestBlock.close", false);

	private int chestState = 0;

	PCChestTypes type;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);
	private final ViewerCountManager stateManager = new ViewerCountManager(){

		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
			PCChestBlockEntity.this.playSound(state, SoundEvents.BLOCK_CHEST_OPEN);
			PCChestBlockEntity.this.setChestState(1);
		}

		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state) {
			PCChestBlockEntity.this.playSound(state, SoundEvents.BLOCK_CHEST_CLOSE);
			PCChestBlockEntity.this.setChestState(2);
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

	public void setChestState(int state){
		this.chestState = state;
	}

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
		AnimationController controller = new AnimationController(this, CONTROLLER_NAME, 3, animationEvent -> {
			switch (this.chestState){
				case ANIM_IDLE:
					animationEvent.getController().setAnimation(IDLE);
					break;
				case ANIM_OPEN:
					animationEvent.getController().setAnimation(OPEN);
					break;
				case ANIM_CLOSE:
					animationEvent.getController().setAnimation(CLOSE);
					break;
				default:
					break;
			}
			return PlayState.CONTINUE;
		});
		data.addAnimationController(controller);
	}

	@SuppressWarnings({ "rawtypes", "resource" })
	@Override
	public void onAnimationSync(int id, int state) {
		if (state == ANIM_OPEN) {
			// Always use GeckoLibUtil to get AnimationControllers when you don't have
			// access to an AnimationEvent
			final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, CONTROLLER_NAME);

			if (controller.getAnimationState() == AnimationState.Stopped) {
				// If you don't do this, the popup animation will only play once because the
				// animation will be cached.
				controller.markNeedsReload();
				// Set the animation to open the JackInTheBoxItem which will start playing music
				// and
				// eventually do the actual animation. Also sets it to not loop
				controller.setAnimation(IDLE);
			}
		}
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
