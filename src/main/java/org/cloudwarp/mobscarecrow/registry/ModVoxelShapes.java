package org.cloudwarp.mobscarecrow.registry;

import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.stream.Stream;

public class ModVoxelShapes {
	public static final VoxelShape CREEPER_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(6, 0, 6, 10, 4, 14),
			Block.createCuboidShape(4.800000000000001, 0.1499999999999999, 6.5, 6.050000000000001, 1.85, 7.850000000000001),
			Block.createCuboidShape(3.4499999999999993, 0.1499999999999999, 5.925000000000001, 4.700000000000001, 1.85, 7.275000000000002),
			Block.createCuboidShape(9.95, 0.1499999999999999, 6.5, 11.2, 1.85, 7.850000000000001),
			Block.createCuboidShape(11.3, 0.1499999999999999, 5.925000000000001, 12.55, 1.85, 7.275000000000002),
			Block.createCuboidShape(9.95, 0.1499999999999999, 11.15, 11.2, 1.85, 12.499999999999998),
			Block.createCuboidShape(11.3, 0.1499999999999999, 11.725, 12.55, 1.85, 13.074999999999998),
			Block.createCuboidShape(4.800000000000001, 0.1499999999999999, 11.15, 6.050000000000001, 1.85, 12.499999999999998),
			Block.createCuboidShape(3.4499999999999993, 0.1499999999999999, 11.725, 4.700000000000001, 1.85, 13.074999999999998),
			Block.createCuboidShape(5.75, 1.4, 2.075, 10.25, 4.5, 4.15),
			Block.createCuboidShape(5.75, 2.325, 3, 10.25, 4.9, 5.6)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	public static final VoxelShape SKELETON_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(5.7044, 4.946900000000001, 5.7212, 10.2576, 7.9047, 6.5244),
			Block.createCuboidShape(6.604399999999999, 3.756900000000001, 6.7162, 9.357600000000001, 6.814700000000001, 7.6194),
			Block.createCuboidShape(6.154399999999999, 4.7369, 4.6262, 9.8076, 7.2597000000000005, 5.5294),
			Block.createCuboidShape(8.904399999999999, 0.001900000000000297, 5.3362, 9.7176, 4.6797, 6.1494),
			Block.createCuboidShape(6.3393999999999995, 0.001900000000000297, 5.3362, 7.1526, 4.6797, 6.1494),
			Block.createCuboidShape(6.7094000000000005, 1.7619000000000002, 9.9712, 9.2526, 2.5397000000000007, 10.8244),
			Block.createCuboidShape(6.179399999999999, 0.8369000000000006, 8.7362, 9.7826, 3.4647000000000006, 9.9694),
			Block.createCuboidShape(6.104399999999999, 2.0269000000000004, 7.5962000000000005, 9.857600000000001, 4.5547, 8.8294),
			Block.createCuboidShape(5.6293999999999995, 5.2069, 3.9862, 10.3326, 6.2197000000000005, 4.8393999999999995),
			Block.createCuboidShape(5.9936, 5.952862336, 2.966173135999999, 10.006400000000001, 9.965662336, 5.975773136000001),
			Block.createCuboidShape(6.9967999999999995, 6.203662336, 0.9597731360000008, 9.0032, 8.210062336, 2.966173135999999),
			Block.createCuboidShape(9.0032, 9.965662336, 3.9693731359999997, 10.006400000000001, 10.968862336, 4.972573136),
			Block.createCuboidShape(5.9936, 9.965662336, 3.9693731359999997, 6.9967999999999995, 10.968862336, 4.972573136),
			Block.createCuboidShape(7.4984, 0.001900000000000297, 12.113119999999999, 8.5016, 1.0050999999999994, 14.119520000000001),
			Block.createCuboidShape(7.693399999999999, 0.7869000000000003, 11.49812, 8.306600000000001, 1.3600999999999996, 12.07452),
			Block.createCuboidShape(7.693399999999999, 1.1669000000000005, 11.118119999999998, 8.306600000000001, 1.7400999999999995, 11.694519999999997)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	public static final VoxelShape SPIDER_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(6.5, 6.75, 2, 9.5, 10.75, 5),
			Block.createCuboidShape(7.075, 8.225000000000001, 0.675, 8.924999999999999, 8.775000000000002, 1.5),
			Block.createCuboidShape(6.25, 4.85, 2.875, 9.75, 7.675, 5.625),
			Block.createCuboidShape(5.6000000000000005, 2.9250000000000003, 5.049999999999999, 10.399999999999999, 6.65, 7.8999999999999995),
			Block.createCuboidShape(4.8, 2.725, 7.699999999999999, 11.2, 5.574999999999999, 10.325),
			Block.createCuboidShape(5.825000000000001, 3.3999999999999995, 11.7, 10.174999999999999, 3.8499999999999996, 12.425),
			Block.createCuboidShape(5.825000000000001, 2.6999999999999997, 12.449999999999998, 10.174999999999999, 3.0999999999999996, 13.174999999999999),
			Block.createCuboidShape(5.825000000000001, 1.8750000000000007, 13.300000000000008, 10.174999999999999, 2.3000000000000007, 14.02500000000001),
			Block.createCuboidShape(8.5, 0, 6, 10.5, 1, 8),
			Block.createCuboidShape(5.5, 0, 6, 7.5, 1, 8),
			Block.createCuboidShape(6, 1, 7, 7, 3, 8),
			Block.createCuboidShape(9, 1, 7, 10, 3, 8)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	public static final VoxelShape ZOMBIE_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(5.25, 0, 6.5, 7.25, 4, 8.5),
			Block.createCuboidShape(4.5, 6, 5, 11.5, 10, 10),
			Block.createCuboidShape(6, 4, 6, 10, 6, 9),
			Block.createCuboidShape(6, 10, 4.5, 10, 15, 8.5),
			Block.createCuboidShape(7.5, 9.25, 3.5, 8.5, 11.25, 4.5),
			Block.createCuboidShape(11.5, 1, 6, 13.5, 10, 9),
			Block.createCuboidShape(2.5, 1, 6, 4.5, 10, 9),
			Block.createCuboidShape(8.75, 0, 6.5, 10.75, 4, 8.5)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	public static final VoxelShape ENDERMITE_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(6.5, 0, 10.5, 9.5, 3, 11.5),
			Block.createCuboidShape(7.5, 0, 11.5, 8.5, 2, 12.5),
			Block.createCuboidShape(6, 0, 3.5, 10, 3, 5.5),
			Block.createCuboidShape(5, 0, 5.5, 11, 4, 10.5)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	public static final VoxelShape TURTLE_SCARECROW_VOXELSHAPE = Stream.of(
			Block.createCuboidShape(0, 1, 5, 5, 2, 8),
			Block.createCuboidShape(6.5, 1, 0, 9.5, 4, 4),
			Block.createCuboidShape(5, 0, 4, 11, 2, 11),
			Block.createCuboidShape(4, 2, 3, 12, 5, 12),
			Block.createCuboidShape(11, 1, 5, 16, 2, 8),
			Block.createCuboidShape(9, 1, 11, 11, 2, 16),
			Block.createCuboidShape(5, 1, 11, 7, 2, 16)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
}
