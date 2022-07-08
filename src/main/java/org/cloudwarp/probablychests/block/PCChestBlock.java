package org.cloudwarp.probablychests.block;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
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
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCProperties;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.BiPredicate;

public class PCChestBlock extends AbstractChestBlock<PCChestBlockEntity> implements Waterloggable {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.5D, 14.0D, 14.0D, 13.0D);
	private final PCChestTypes type;


	public PCChestBlock (Settings settings, PCChestTypes type) {
		super(settings, type::getBlockEntityType);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(WATERLOGGED, false).with(CHEST_STATE, PCChestState.CLOSED));
		this.type = type;
	}

	public static boolean isChestBlocked (WorldAccess world, BlockPos pos) {
		return PCChestBlock.hasBlockOnTop(world, pos);
	}

	private static boolean hasBlockOnTop (BlockView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		return world.getBlockState(blockPos).isSolidBlock(world, blockPos);
	}

	public void onBlockBreakStart (BlockState state, World world, BlockPos pos, PlayerEntity player) {
		createMimic(world, pos, state);
	}

	public void onEntityCollision (BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity) {
			createMimic(world, pos, state);
		}
	}

	private boolean createMimic (World world, BlockPos pos, BlockState state) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			boolean wasSecretMimic = checkForSecretMimic(chest, world, pos);
			if (!wasSecretMimic){
				return false;
			}
			if (world.getDifficulty() != Difficulty.PEACEFUL && chest.isMimic) {
				createMimic(false,pos,state,world,chest, null);
				return true;
			}
		}
		return false;
	}
	public void createMimic(boolean isPetMimic, BlockPos pos, BlockState state, World world, PCChestBlockEntity chest, PlayerEntity player){
		if(chest.hasMadeMimic){
			return;
		}
		chest.hasMadeMimic = true;
		PCTameablePetWithInventory mimic;
		if(isPetMimic){
			mimic = new PCChestMimicPet(this.type.getPetMimicType(), world);
			mimic.setOwner(player);
			mimic.setTarget((LivingEntity) null);
			mimic.setSitting(true);
		}else{
			mimic = new PCChestMimic(this.type.getMimicType(), world);
		}
		mimic.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
		mimic.setYaw(state.get(FACING).asRotation());
		mimic.headYaw = mimic.getYaw();
		mimic.bodyYaw = mimic.getYaw();
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setStack(i, chest.getStack(i));
			chest.setStack(i, ItemStack.EMPTY);
		}
		world.spawnEntity(mimic);
		if(isPetMimic){
			mimic.world.sendEntityStatus(mimic, (byte) 7);
		}
		boolean waterlogged = state.get(WATERLOGGED);
		world.setBlockState(pos, waterlogged ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
	}
	public boolean checkForSecretMimic (PCChestBlockEntity chest, World world, BlockPos pos) {
		PCConfig config = ProbablyChests.loadedConfig;
		if(chest.isMimic){
			return true;
		}
		if (! chest.hasBeenOpened && chest.isNatural) {
			chest.hasBeenOpened = true;
			float mimicRandom = world.getRandom().nextFloat();
			chest.isMimic = mimicRandom < config.worldGen.secretMimicChance;
			if (chest.isMimic) {
				return true;
			}
			LootableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, this.type.getLootTable());
		}
		return false;
	}

	private boolean createPetMimic (World world, BlockPos pos, BlockState state, PlayerEntity player) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			boolean wasSecretMimic = checkForSecretMimic(chest, world, pos);
			if(wasSecretMimic){
				return false;
			}
			createMimic(true,pos,state,world,chest, player);
			return true;
		}
		return false;
	}

	public PCChestBlockEntity getChestBlockFromWorld (World world, BlockPos pos) {
		PCChestBlockEntity chest = null;
		if (world.getBlockEntity(pos) instanceof PCChestBlockEntity) {
			chest = (PCChestBlockEntity) world.getBlockEntity(pos);
		}
		return chest;
	}


	@Override
	public ActionResult onUse (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		PCConfig config = ProbablyChests.loadedConfig;
		//------------------------
		if (chest != null) {
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.isOf(PCItems.PET_MIMIC_KEY) && config.mimicSettings.allowPetMimics && ! player.isSneaking()) {
				createPetMimic(world, pos, state, player);
				if (! player.isCreative()) {
					itemStack.decrement(1);
				}
				return ActionResult.CONSUME;
			} else if (itemStack.isOf(PCItems.MIMIC_KEY) && ! chest.isMimic && ! player.isSneaking()) {
				chest.isMimic = true;
				if (! player.isCreative()) {
					itemStack.decrement(1);
				}
				return ActionResult.CONSUME;
			} else if (createMimic(world, pos, state)) {
				return ActionResult.SUCCESS;
			}
		}
		NamedScreenHandlerFactory namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
		if (namedScreenHandlerFactory != null) {
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
		if (! createMimic(world, pos, state)) {
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory) ((Object) blockEntity));
				world.updateComparators(pos, this);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onPlaced (World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world,pos);
		if (chest != null && itemStack.hasCustomName()) {
			chest.setCustomName(itemStack.getName());
		}
	}


	@Override
	public VoxelShape getOutlineShape (BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}


	public BlockEntity createBlockEntity (BlockPos pos, BlockState state) {
		return this.type.makeEntity(pos, state);
	}


	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	public BlockState getPlacementState (ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return (this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite())).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, CHEST_STATE);
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

	public static Direction getFacing(BlockState state) {
		return state.get(FACING);
	}


	@Override
	public DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> getBlockEntitySource (BlockState state, World world, BlockPos pos, boolean ignoreBlocked) {
		return DoubleBlockProperties.PropertyRetriever::getFallback;
	}

}