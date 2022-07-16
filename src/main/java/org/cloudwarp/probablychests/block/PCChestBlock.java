package org.cloudwarp.probablychests.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.cloudwarp.probablychests.utils.PCLockedState;
import org.cloudwarp.probablychests.utils.VoxelShaper;

import java.util.Map;
import java.util.Random;

import static org.cloudwarp.probablychests.utils.PCMimicCreationUtils.*;

public class PCChestBlock extends AbstractChestBlock<PCChestBlockEntity> implements Waterloggable {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;
	public static final EnumProperty<PCLockedState> LOCKED_STATE = PCProperties.PC_LOCKED_STATE;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.5D, 15.0D, 14.0D, 14.5D);
	protected static final Map<Direction, VoxelShape> SHAPES = VoxelShaper.generateRotations(SHAPE);
	private final PCChestTypes type;


	public PCChestBlock (Settings settings, PCChestTypes type) {
		super(settings, type::getBlockEntityType);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(WATERLOGGED, false).with(CHEST_STATE, PCChestState.CLOSED).with(LOCKED_STATE,PCLockedState.UNLOCKED));
		this.type = type;
	}

	public static boolean isChestBlocked (WorldAccess world, BlockPos pos) {
		return PCChestBlock.hasBlockOnTop(world, pos);
	}

	public static boolean isDry (BlockState state) {
		return ! state.get(WATERLOGGED);
	}

	private static boolean hasBlockOnTop (BlockView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
	}

	public void onBlockBreakStart (BlockState state, World world, BlockPos pos, PlayerEntity player) {
		tryMakeHostileMimic(world, pos, state, player, this.type);
	}

	public void onEntityCollision (BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity player) {
			tryMakeHostileMimic(world, pos, state, player, this.type);
		}
	}

	@Override
	public float getHardness () {
		if(this.stateManager.getProperty(PCProperties.PC_LOCKED_STATE.getName()).equals(PCLockedState.LOCKED)){
			return -1F;
		}
		return 2.0F;
	}

	@Override
	public float calcBlockBreakingDelta (BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		float f = state.get(PCProperties.PC_LOCKED_STATE).equals(PCLockedState.LOCKED) ? -1F : 2.0F;
		if (f == -1.0f) {
			return 0.0f;
		}
		int i = player.canHarvest(state) ? 30 : 100;
		return player.getBlockBreakingSpeed(state) / f / (float)i;
	}

	public boolean unlockBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit){
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		ItemStack itemStack = player.getStackInHand(hand);
		if (chest.hasGoldLock && itemStack.isOf(PCItems.GOLD_KEY)) {
			chest.isLocked = false;
			PCChestBlockEntity.playSound(world,pos,state,PCSounds.LOCK_UNLOCK, 1.3f);
			return true;
		} else if (chest.hasVoidLock && itemStack.isOf(PCItems.VOID_KEY)) {
			chest.isLocked = false;
			PCChestBlockEntity.playSound(world,pos,state,PCSounds.LOCK_UNLOCK, 1.3f);
			return true;
		} else {
			PCChestBlockEntity.playSound(world,pos,state,PCSounds.APPLY_LOCK2, 1.0f);
			return false;
		}
	}
	public boolean lockBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit){
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		ItemStack itemStack = player.getStackInHand(hand);
		if (chest.hasGoldLock && itemStack.isOf(PCItems.GOLD_KEY)) {
			chest.isLocked = true;
			PCChestBlockEntity.playSound(world,pos,state,PCSounds.LOCK_UNLOCK, 0.6f);
			return true;
		} else if (chest.hasVoidLock && itemStack.isOf(PCItems.VOID_KEY)) {
			chest.isLocked = true;
			PCChestBlockEntity.playSound(world,pos,state,PCSounds.LOCK_UNLOCK, 0.6f);
			return true;
		} else {
			return false;
		}
	}
	public boolean addLockToBlock (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit){
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		ItemStack itemStack = player.getStackInHand(hand);
		if (!chest.hasGoldLock && !chest.hasVoidLock && !chest.isLocked) {
			if(itemStack.isOf(PCItems.GOLD_LOCK) && chest.type().equals(PCChestTypes.GOLD)){
				chest.isLocked = true;
				chest.hasGoldLock = true;
				PCChestBlockEntity.playSound(world,pos,state,PCSounds.APPLY_LOCK1, 0.6f);
				return true;
			}else if(itemStack.isOf(PCItems.VOID_LOCK) && chest.type().equals(PCChestTypes.SHADOW)){
				chest.isLocked = true;
				chest.hasVoidLock = true;
				PCChestBlockEntity.playSound(world,pos,state,PCSounds.APPLY_LOCK1, 0.6f);
				return true;
			}
		}
		return false;
	}
	public void lockBlockState(BlockState state, World world, BlockPos pos){
		world.setBlockState(pos, state.with(PCProperties.PC_LOCKED_STATE,PCLockedState.LOCKED));
	}
	public void unlockBlockState(BlockState state, World world, BlockPos pos){
		world.setBlockState(pos, state.with(PCProperties.PC_LOCKED_STATE,PCLockedState.UNLOCKED));
	}

	@Override
	public ActionResult onUse (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		PCConfig config = ProbablyChests.loadedConfig;
		ItemStack itemStack = player.getStackInHand(hand);
		if (chest.isLocked) {
			if(unlockBlock(state, world, pos, player, hand, hit)){
				unlockBlockState(state,world,pos);
				return ActionResult.CONSUME;
			}else{
				return ActionResult.FAIL;
			}
		}else{
			if(addLockToBlock(state, world, pos, player, hand, hit)){
				lockBlockState(state,world,pos);
				return ActionResult.CONSUME;
			}
			if(lockBlock(state, world, pos, player, hand, hit)){
				lockBlockState(state,world,pos);
				return ActionResult.CONSUME;
			}
		}
		//------------------------
		if (chest != null) {
			if (itemStack.isOf(PCItems.PET_MIMIC_KEY) && config.mimicSettings.allowPetMimics && ! player.isSneaking() && tryMakePetMimic(world, pos, state, player, this.type)) {
				if (! player.isCreative()) {
					itemStack.decrement(1);
				}
				return ActionResult.CONSUME;
			} else if (itemStack.isOf(PCItems.MIMIC_KEY) && ! player.isSneaking() && ! isSecretMimic(chest, world, pos, this.type) && world.getDifficulty() != Difficulty.PEACEFUL) {
				chest.isMimic = true;
				chest.isNatural = false;
				if (! player.isCreative()) {
					itemStack.decrement(1);
				}
				return ActionResult.CONSUME;
			} else {
				if (isSecretMimic(chest, world, pos, type)) {
					tryMakeHostileMimic(world, pos, state, player, this.type);
					return ActionResult.SUCCESS;
				}
			}
		}
		NamedScreenHandlerFactory namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
		if (namedScreenHandlerFactory != null && player instanceof ServerPlayerEntity && chest.checkUnlocked(player)) {
			player.openHandledScreen(namedScreenHandlerFactory);
			player.incrementStat(this.getOpenStat());
		}
		return ActionResult.CONSUME;
	}

	protected Stat<Identifier> getOpenStat () {
		return Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST);
	}

	@Override
	public boolean hasComparatorOutput (BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput (BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public void onStateReplaced (BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (! isSecretMimic(chest, world, pos, this.type)) {
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory) ((Object) blockEntity));
				world.updateComparators(pos, this);
			}
		} else {
			tryMakeHostileMimic(world, pos, state, null, this.type);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onPlaced (World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null && itemStack.hasCustomName()) {
			chest.setCustomName(itemStack.getName());
		}
	}


	@Override
	public VoxelShape getOutlineShape (BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(FACING));
	}


	public BlockEntity createBlockEntity (BlockPos pos, BlockState state) {
		return this.type.makeEntity(pos, state);
	}


	@Override
	public BlockRenderType getRenderType (BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}


	public BlockState getPlacementState (ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return (this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite())).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, CHEST_STATE, LOCKED_STATE);
		super.appendProperties(builder);
	}

	@Override
	public FluidState getFluidState (BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForNeighborUpdate (BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}


	@Override
	public void scheduledTick (BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PCChestBlockEntity chestBlockEntity) {
			chestBlockEntity.onScheduledTick();
		}

	}

	public BlockEntityType<? extends PCChestBlockEntity> getExpectedEntityType () {
		return this.entityTypeRetriever.get();
	}

	public static Direction getFacing (BlockState state) {
		return state.get(FACING);
	}


	@Override
	public DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> getBlockEntitySource (BlockState state, World world, BlockPos pos, boolean ignoreBlocked) {
		return DoubleBlockProperties.PropertyRetriever::getFallback;
	}

}