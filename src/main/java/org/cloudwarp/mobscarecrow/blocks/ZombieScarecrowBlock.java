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

public class ZombieScarecrowBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	protected static final VoxelShape NORTH_SHAPE;
	protected static final VoxelShape EAST_SHAPE;
	protected static final VoxelShape SOUTH_SHAPE;
	protected static final VoxelShape WEST_SHAPE;

	static {
		FACING = HorizontalFacingBlock.FACING;
		NORTH_SHAPE = Stream.of(
				Block.createCuboidShape(2, 0, 6, 14, 10, 9),
				Block.createCuboidShape(6, 10, 4.5, 10, 15, 8.5),
				Block.createCuboidShape(4, 6, 5, 12, 10, 10)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		EAST_SHAPE = Stream.of(
				Block.createCuboidShape(7, 0, 2, 10, 10, 14),
				Block.createCuboidShape(7.5, 10, 6, 11.5, 15, 10),
				Block.createCuboidShape(6, 6, 4, 11, 10, 12)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		SOUTH_SHAPE = Stream.of(
				Block.createCuboidShape(2, 0, 7, 14, 10, 10),
				Block.createCuboidShape(6, 10, 7.5, 10, 15, 11.5),
				Block.createCuboidShape(4, 6, 6, 12, 10, 11)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		WEST_SHAPE = Stream.of(
				Block.createCuboidShape(6, 0, 2, 9, 10, 14),
				Block.createCuboidShape(4.5, 10, 6, 8.5, 15, 10),
				Block.createCuboidShape(5, 6, 4, 10, 10, 12)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	public ZombieScarecrowBlock (Settings settings) {
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