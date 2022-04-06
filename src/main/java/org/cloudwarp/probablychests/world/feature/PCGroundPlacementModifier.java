package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

public class PCGroundPlacementModifier extends PlacementModifier {


	public static final Codec<PCGroundPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
					(Direction.VERTICAL_CODEC.fieldOf("direction_of_search")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.direction),
					(BlockPredicate.BASE_CODEC.fieldOf("direction_target_condition")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.directionPredicate),
					(BlockPredicate.BASE_CODEC.fieldOf("direction_target_block_condition")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.directionBlockPredicate),
					(BlockPredicate.BASE_CODEC.fieldOf("target_condition")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.targetPredicate),
					(Codec.intRange(1, 32).fieldOf("max_steps")).forGetter(PCGroundPlacementModifier -> PCGroundPlacementModifier.maxSteps),
					Heightmap.Type.CODEC.fieldOf("heightmap").forGetter((PCGroundPlacementModifier) -> PCGroundPlacementModifier.heightmap))
			.apply(instance, PCGroundPlacementModifier::new));
	//---------------------------------------------------
	private final Direction direction;
	private final BlockPredicate directionPredicate;
	private final BlockPredicate directionBlockPredicate;
	private final BlockPredicate targetPredicate;
	private final int maxSteps;
	private final Heightmap.Type heightmap;


	private PCGroundPlacementModifier (Direction direction, BlockPredicate directionPredicate, BlockPredicate directionBlockPredicate, BlockPredicate targetPredicate, int maxSteps, Heightmap.Type heightmap) {
		this.direction = direction;
		this.targetPredicate = targetPredicate;
		this.directionPredicate = directionPredicate;
		this.directionBlockPredicate = directionBlockPredicate;
		this.maxSteps = maxSteps;
		this.heightmap = heightmap;
	}

	public static PCGroundPlacementModifier of (Direction direction, BlockPredicate directionPredicate, BlockPredicate directionBlockPredicate, BlockPredicate targetPredicate, int maxSteps, Heightmap.Type heightmap) {
		return new PCGroundPlacementModifier(direction, directionPredicate, directionBlockPredicate, targetPredicate, maxSteps, heightmap);
	}

	@Override
	public Stream<BlockPos> getPositions (FeaturePlacementContext context, Random random, BlockPos pos) {
		BlockPos.Mutable mutableTarget = pos.mutableCopy();
		int k = context.getTopY(this.heightmap, mutableTarget.getX(), mutableTarget.getZ());
		mutableTarget.set(mutableTarget.getX(),k - 1,mutableTarget.getZ());
		BlockPos.Mutable mutableDirection = mutableTarget.mutableCopy();
		mutableDirection.move(Direction.DOWN);
		StructureWorldAccess structureWorldAccess = context.getWorld();
		// &&
		//					this.directionBlockPredicate.test(structureWorldAccess, mutableDirection)

		for (int i = 0; i < this.maxSteps; ++ i) {
			if (this.targetPredicate.test(structureWorldAccess, mutableTarget) &&
					this.directionPredicate.test(structureWorldAccess, mutableDirection) &&
					! BlockPredicate.matchingBlock(Blocks.BEDROCK, BlockPos.ORIGIN).test(structureWorldAccess, mutableDirection)) {
				return Stream.of(mutableTarget);
			}
			mutableTarget.move(this.direction);
			mutableDirection.move(this.direction);
			if (structureWorldAccess.isOutOfHeightLimit(mutableTarget.getY())) {
				return Stream.of(new BlockPos[0]);
			}
		}
		if (this.targetPredicate.test(structureWorldAccess, mutableTarget) &&
				this.directionPredicate.test(structureWorldAccess, mutableDirection) &&
				! BlockPredicate.matchingBlock(Blocks.BEDROCK, BlockPos.ORIGIN).test(structureWorldAccess, mutableDirection)) {
			return Stream.of(mutableTarget);
		}
		return Stream.of(new BlockPos[0]);
	}

	@Override
	public PlacementModifierType<?> getType () {
		return PCPlacementModifierType.CHEST_SCAN;
	}
}
