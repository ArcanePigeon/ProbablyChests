package org.cloudwarp.mobscarecrow;

import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class VoxelShaper {
	protected static VoxelShape rotatedCopy (VoxelShape shape, float rotation) {

		Vec3d center = new Vec3d(8, 8, 8);
		final Stream<VoxelShape>[] stream = new Stream[] {Stream.<VoxelShape>empty()};
		shape.forEachBox((x1, y1, z1, x2, y2, z2) -> {
			Vec3d v1 = new Vec3d(x1, y1, z1).multiply(16).subtract(center);
			Vec3d v2 = new Vec3d(x2, y2, z2).multiply(16).subtract(center);
			v1 = rotate(v1, rotation).add(center);
			v2 = rotate(v2, rotation).add(center);
			if(v1.x > v2.x){
				Vec3d tmp = v1;
				v1 = new Vec3d(v2.x,v1.y,v1.z);
				v2 = new Vec3d(tmp.x,v2.y,v2.z);
			}
			if(v1.z > v2.z){
				Vec3d tmp = v1;
				v1 = new Vec3d(v1.x,v1.y,v2.z);
				v2 = new Vec3d(v2.x,v2.y,tmp.z);
			}
			VoxelShape rotated = Block.createCuboidShape(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
			stream[0] = Stream.concat(stream[0], Stream.of(rotated));
		});
		return stream[0].reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	private static Vec3d rotate (Vec3d v, float rotation) {
		double rot = Math.toRadians(rotation);
		return new Vec3d(v.x * Math.cos(rot) - v.z * Math.sin(rot), v.y, v.x * Math.sin(rot) + v.z * Math.cos(rot));
	}

	public static Map<Direction, VoxelShape> generateRotations (VoxelShape shape) {
		Map<Direction, VoxelShape> shapes = new HashMap<>();
		shapes.put(Direction.NORTH, shape);
		shapes.put(Direction.EAST, rotatedCopy(shape, 90f));
		shapes.put(Direction.SOUTH, rotatedCopy(shape, 180f));
		shapes.put(Direction.WEST, rotatedCopy(shape, 270f));
		return shapes;
	}
}
