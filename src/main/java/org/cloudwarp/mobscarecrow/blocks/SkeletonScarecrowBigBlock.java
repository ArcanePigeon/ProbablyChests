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
import org.cloudwarp.mobscarecrow.VoxelShaper;
import org.cloudwarp.mobscarecrow.blockentities.MobScarecrowBlockEntity;
import org.cloudwarp.mobscarecrow.blockentities.SkeletonScarecrowBigBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SkeletonScarecrowBigBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF;
	protected static final VoxelShape SHAPE;
	private static Map<Direction, VoxelShape> shapes;

	static {
		FACING = HorizontalFacingBlock.FACING;
		HALF = Properties.DOUBLE_BLOCK_HALF;
		SHAPE = Stream.of(
				Block.createCuboidShape(3.379999999999999, 0.005000000000000782, 0.7400000000000002, 6.02, 10.565000000000001, 3.379999999999999),
				Block.createCuboidShape(1.379999999999999, 9.755, 1.7400000000000002, 14.52, 21.065, 4.379999999999999),
				Block.createCuboidShape(1.379999999999999, 12.005, - 1.0099999999999998, 14.52, 19.565, 1.629999999999999),
				Block.createCuboidShape(1.379999999999999, 7.005000000000001, 4.49, 14.52, 18.315, 7.129999999999999),
				Block.createCuboidShape(1.379999999999999, 8.505, 2.99, 14.52, 19.815, 5.629999999999999),
				Block.createCuboidShape(1.379999999999999, 4.255000000000001, 7.24, 14.52, 12.565000000000001, 9.879999999999999),
				Block.createCuboidShape(1.379999999999999, 5.505000000000001, 5.99, 14.52, 13.815000000000001, 8.629999999999999),
				Block.createCuboidShape(1.379999999999999, 1.5050000000000008, 9.99, 14.52, 9.815000000000001, 12.629999999999999),
				Block.createCuboidShape(1.379999999999999, 2.755000000000001, 8.74, 14.52, 11.065000000000001, 11.379999999999999),
				Block.createCuboidShape(1.379999999999999, 4.755000000000001, 12.99, 14.52, 6.815000000000001, 15.629999999999999),
				Block.createCuboidShape(1.379999999999999, 3.005000000000001, 11.49, 14.52, 8.315000000000001, 14.129999999999999),
				Block.createCuboidShape(1.379999999999999, 13.505, - 2.76, 14.52, 16.565, - 0.120000000000001),
				Block.createCuboidShape(9.98, 0.005000000000000782, 0.7400000000000002, 12.62, 10.565000000000001, 3.379999999999999),
				Block.createCuboidShape(2.7199999999999998, 15.6654272, - 5.246912800000002, 13.280000000000001, 26.2254272, 2.673087200000001),
				Block.createCuboidShape(5.359999999999999, 16.3254272, - 10.526912799999998, 10.64, 21.6054272, - 5.246912800000002),
				Block.createCuboidShape(10.64, 26.2254272, - 2.6069128000000017, 13.280000000000001, 28.865427200000003, 0.03308720000000065),
				Block.createCuboidShape(2.7199999999999998, 26.2254272, - 2.6069128000000017, 5.359999999999999, 28.865427200000003, 0.03308720000000065),
				Block.createCuboidShape(6.68, 0.005000000000000782, 18.823999999999998, 9.32, 2.6449999999999987, 24.104000000000003),
				Block.createCuboidShape(11.68, 0.005000000000000782, 9.073999999999998, 13.32, 2.6449999999999987, 11.104000000000003),
				Block.createCuboidShape(12.43, 0.005000000000000782, 7.323999999999998, 14.07, 2.6449999999999987, 9.354000000000003),
				Block.createCuboidShape(13.18, 0.005000000000000782, 5.323999999999998, 14.82, 2.6449999999999987, 7.354000000000003),
				Block.createCuboidShape(13.68, 0.005000000000000782, 4.073999999999998, 15.32, 2.6449999999999987, 6.104000000000003),
				Block.createCuboidShape(0.6799999999999997, 0.005000000000000782, 4.073999999999998, 2.3200000000000003, 2.6449999999999987, 6.104000000000003),
				Block.createCuboidShape(1.1799999999999997, 0.005000000000000782, 5.323999999999998, 2.8200000000000003, 2.6449999999999987, 7.354000000000003),
				Block.createCuboidShape(1.9299999999999997, 0.005000000000000782, 7.323999999999998, 3.5700000000000003, 2.6449999999999987, 9.354000000000003),
				Block.createCuboidShape(2.6799999999999997, 0.005000000000000782, 9.073999999999998, 4.32, 2.6449999999999987, 11.104000000000003),
				Block.createCuboidShape(6.68, 1.0050000000000008, 17.823999999999998, 9.32, 2.6449999999999987, 20.104000000000003),
				Block.createCuboidShape(6.68, 2.505000000000001, 16.073999999999998, 9.32, 4.394999999999999, 18.354000000000003),
				Block.createCuboidShape(6.68, 3.755000000000001, 14.573999999999998, 9.32, 5.644999999999999, 16.854000000000003)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	public SkeletonScarecrowBigBlock (Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
		shapes = VoxelShaper.generateRotations(SHAPE);
	}

	@Override
	public VoxelShape getOutlineShape (BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		//boolean lower = state.get(HALF) == DoubleBlockHalf.LOWER;
		// Set voxel shape of scarecrow half based on place direction.
		return shapes.get(direction);
	}

	/*public void onPlaced (World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		//world.setBlockState(pos.up(), (BlockState)state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			super.onPlaced(world, pos, state, placer, itemStack);
			return;
		}
		// If placing bottom half, place top half above block pos.
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER));
	}*/

	/*public void onBreak (World world, BlockPos pos, BlockState state, PlayerEntity player) {
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
	}*/

	/*@Override
	public void onStateReplaced (BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		BlockPos newPos;

		if (state.getBlock() != this) {
			super.onStateReplaced(state, world, pos, newState, moved);
			return;
		}
		// Find block entity position.
		if (state.get(SkeletonScarecrowBigBlock.HALF) == DoubleBlockHalf.UPPER) {
			newPos = pos.down();
		} else {
			newPos = pos.up();
		}
		// Remove entity and set new block state.
		if (! (newState.getBlock() instanceof SkeletonScarecrowBigBlock)) {
			world.removeBlockEntity(newPos);
			world.setBlockState(newPos, newState);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}*/

	@Override
	public PistonBehavior getPistonBehavior (BlockState state) {
		return PistonBehavior.DESTROY;
	}

	/*@Override
	public boolean canPlaceAt (BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return state.get(HALF) == DoubleBlockHalf.LOWER ? blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
	}*/

	@Override
	public BlockState getPlacementState (ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}

	protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity (BlockPos pos, BlockState state) {
		return new SkeletonScarecrowBigBlockEntity(pos, state);
	}
}