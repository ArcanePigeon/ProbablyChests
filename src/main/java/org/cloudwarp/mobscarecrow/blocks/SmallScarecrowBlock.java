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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.cloudwarp.mobscarecrow.blockdetails.MobScarecrowBlockTags;
import org.cloudwarp.mobscarecrow.blockentities.SmallScarecrowBlockEntity;
import org.cloudwarp.mobscarecrow.registry.ModSounds;
import org.cloudwarp.mobscarecrow.utils.VoxelShaper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SmallScarecrowBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	private static VoxelShape SHAPE;
	private Map<Direction, VoxelShape> shapes;

	static {
		FACING = HorizontalFacingBlock.FACING;
	}

	public SmallScarecrowBlock (Settings settings, VoxelShape voxelShape) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
		SHAPE = voxelShape;
		shapes = VoxelShaper.generateRotations(SHAPE);
	}

	@Override
	public VoxelShape getOutlineShape (BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		return shapes.get(direction);
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
		return new SmallScarecrowBlockEntity(pos, state);
	}
}