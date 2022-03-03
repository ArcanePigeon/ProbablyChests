package org.cloudwarp.mobscarecrow.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.cloudwarp.mobscarecrow.blockentities.MobScarecrowBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MobScarecrowBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF;
	protected static final VoxelShape NORTH_SHAPE_UPPER;
	protected static final VoxelShape EAST_SHAPE_UPPER;
	protected static final VoxelShape NORTH_SHAPE_LOWER;
	protected static final VoxelShape EAST_SHAPE_LOWER;

	static {
		FACING = HorizontalFacingBlock.FACING;
		HALF = Properties.DOUBLE_BLOCK_HALF;
		NORTH_SHAPE_LOWER = Stream.of(
				Block.createCuboidShape(4, 8, 6, 12, 16, 10),
				Block.createCuboidShape(7, 0, 7, 9, 8, 9)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		EAST_SHAPE_LOWER = Stream.of(
				Block.createCuboidShape(6, 8, 4, 10, 16, 12),
				Block.createCuboidShape(7, 0, 7, 9, 8, 9)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		NORTH_SHAPE_UPPER = Stream.of(
				Block.createCuboidShape(3, 4, 3, 13, 13, 13),
				Block.createCuboidShape(4, 0, 6, 12, 4, 10)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		EAST_SHAPE_UPPER = Stream.of(
				Block.createCuboidShape(3, 4, 3, 13, 13, 13),
				Block.createCuboidShape(6, 0, 4, 10, 4, 12)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	public MobScarecrowBlock (Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public VoxelShape getOutlineShape (BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		boolean lower = state.get(HALF) == DoubleBlockHalf.LOWER;
		// Set voxel shape of scarecrow half based on place direction.
		if (direction == Direction.NORTH || direction == Direction.SOUTH) {
			return lower ? NORTH_SHAPE_LOWER : NORTH_SHAPE_UPPER;
		} else {
			return lower ? EAST_SHAPE_LOWER : EAST_SHAPE_UPPER;
		}
	}

	public void onPlaced (World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		//world.setBlockState(pos.up(), (BlockState)state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			super.onPlaced(world, pos, state, placer, itemStack);
			return;
		}
		// If placing bottom half, place top half above block pos.
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER));
	}

	public void onBreak (World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos topPos;
		BlockPos botPos;
		// Get block position of both halves.
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			topPos = pos;
			botPos = pos.down();
		} else {
			topPos = pos.up();
			botPos = pos;
		}
		// If block entity is of type mobscarecrow then spawn items at break position.
		if (world.getBlockEntity(botPos) instanceof MobScarecrowBlockEntity) {
			if (! player.isCreative() && player.canHarvest(world.getBlockState(botPos)) && world instanceof ServerWorld) {
				if (! world.isClient) {
					ItemStack itemStack = new ItemStack(state.getBlock().asItem());
					ItemScatterer.spawn(world, (double) topPos.getX() + 0.5D, (double) topPos.getY() + 0.5D, (double) topPos.getZ() + 0.5D, itemStack);
				}
			}
			world.removeBlockEntity(botPos);
		}
		// If block entity is of type mobscarecrow then remove top half.
		if (world.getBlockEntity(topPos) instanceof MobScarecrowBlockEntity) {
			world.removeBlockEntity(topPos);
		}
		// Remove blocks halves.
		world.removeBlock(topPos, false);
		world.removeBlock(botPos, false);
		world.updateNeighbors(topPos, Blocks.AIR);

		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onStateReplaced (BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		BlockPos newPos;

		if (state.getBlock() != this) {
			super.onStateReplaced(state, world, pos, newState, moved);
			return;
		}
		// Find block entity position.
		if (state.get(MobScarecrowBlock.HALF) == DoubleBlockHalf.UPPER) {
			newPos = pos.down();
		} else {
			newPos = pos.up();
		}
		// Remove entity and set new block state.
		if (! (newState.getBlock() instanceof MobScarecrowBlock)) {
			world.removeBlockEntity(newPos);
			world.setBlockState(newPos, newState);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public PistonBehavior getPistonBehavior (BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	public boolean canPlaceAt (BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return state.get(HALF) == DoubleBlockHalf.LOWER ? blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
	}

	@Override
	public BlockState getPlacementState (ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}

	protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF, FACING);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity (BlockPos pos, BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.UPPER ? null : new MobScarecrowBlockEntity(pos, state);
	}
}