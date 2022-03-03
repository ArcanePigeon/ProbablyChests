package org.cloudwarp.mobscarecrow.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.cloudwarp.mobscarecrow.blockdetails.MobScarecrowBlockTags;
import org.cloudwarp.mobscarecrow.blockentities.MobScarecrowBlockEntity;
import org.cloudwarp.mobscarecrow.registry.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SkeletonScarecrowBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	protected static final VoxelShape NORTH_SHAPE;
	protected static final VoxelShape EAST_SHAPE;
	protected static final VoxelShape SOUTH_SHAPE;
	protected static final VoxelShape WEST_SHAPE;

	static {
		FACING = HorizontalFacingBlock.FACING;
		NORTH_SHAPE = Stream.of(
				Block.createCuboidShape(6, 6, 3, 10, 10, 6),
				Block.createCuboidShape(7, 6.25, 1, 9, 8.25, 3),
				Block.createCuboidShape(6, 3, 6, 10, 6, 9),
				Block.createCuboidShape(6, 4, 4, 10, 8, 7),
				Block.createCuboidShape(8.75, 0, 5.25, 9.75, 4, 6.25),
				Block.createCuboidShape(6.25, 0, 5.25, 7.25, 4, 6.25),
				Block.createCuboidShape(6, 1, 8, 10, 4, 11),
				Block.createCuboidShape(6, 0, 8, 10, 2, 12),
				Block.createCuboidShape(7.5, 0, 12, 8.5, 1, 14)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		EAST_SHAPE = Stream.of(
				Block.createCuboidShape(10, 6, 6, 13, 10, 10),
				Block.createCuboidShape(13, 6.25, 7, 15, 8.25, 9),
				Block.createCuboidShape(7, 3, 6, 10, 6, 10),
				Block.createCuboidShape(9, 4, 6, 12, 8, 10),
				Block.createCuboidShape(9.75, 0, 8.75, 10.75, 4, 9.75),
				Block.createCuboidShape(9.75, 0, 6.25, 10.75, 4, 7.25),
				Block.createCuboidShape(5, 1, 6, 8, 4, 10),
				Block.createCuboidShape(4, 0, 6, 8, 2, 10),
				Block.createCuboidShape(2, 0, 7.5, 4, 1, 8.5)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		SOUTH_SHAPE = Stream.of(
				Block.createCuboidShape(6, 6, 10, 10, 10, 13),
				Block.createCuboidShape(7, 6.25, 13, 9, 8.25, 15),
				Block.createCuboidShape(6, 3, 7, 10, 6, 10),
				Block.createCuboidShape(6, 4, 9, 10, 8, 12),
				Block.createCuboidShape(6.25, 0, 9.75, 7.25, 4, 10.75),
				Block.createCuboidShape(8.75, 0, 9.75, 9.75, 4, 10.75),
				Block.createCuboidShape(6, 1, 5, 10, 4, 8),
				Block.createCuboidShape(6, 0, 4, 10, 2, 8),
				Block.createCuboidShape(7.5, 0, 2, 8.5, 1, 4)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		WEST_SHAPE = Stream.of(
				Block.createCuboidShape(3, 6, 6, 6, 10, 10),
				Block.createCuboidShape(1, 6.25, 7, 3, 8.25, 9),
				Block.createCuboidShape(6, 3, 6, 9, 6, 10),
				Block.createCuboidShape(4, 4, 6, 7, 8, 10),
				Block.createCuboidShape(5.25, 0, 6.25, 6.25, 4, 7.25),
				Block.createCuboidShape(5.25, 0, 8.75, 6.25, 4, 9.75),
				Block.createCuboidShape(8, 1, 6, 11, 4, 10),
				Block.createCuboidShape(8, 0, 6, 12, 2, 10),
				Block.createCuboidShape(12, 0, 7.5, 14, 1, 8.5)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	public SkeletonScarecrowBlock (Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getOutlineShape (BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		if (direction == Direction.NORTH) {
			return NORTH_SHAPE;
		} else if (direction == Direction.EAST) {
			return EAST_SHAPE;
		} else if (direction == Direction.SOUTH) {
			return SOUTH_SHAPE;
		} else {
			return WEST_SHAPE;
		}
	}

	@Override
	public ActionResult onUse (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (! state.isIn(MobScarecrowBlockTags.PLUSHIE)) {
			return ActionResult.success(world.isClient);
		}
		if (! world.isClient) {
			world.playSound(null, pos, ModSounds.PLUSHIE_SQUEAK_EVENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public PistonBehavior getPistonBehavior (BlockState state) {
		return PistonBehavior.DESTROY;
	}

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
		return new MobScarecrowBlockEntity(pos, state);
	}
}