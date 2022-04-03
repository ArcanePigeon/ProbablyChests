package org.cloudwarp.probablychests.world.feature;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

public class PCChestPlacement extends PlacementModifier {


	private final Direction direction;
	private final BlockPredicate directionPredicate;
	private final BlockPredicate targetPredicate;
	private final int maxSteps;

	public static final Codec<PCChestPlacement> MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			((MapCodec)Direction.VERTICAL_CODEC.fieldOf("direction_of_search")).forGetter(PCChestPlacement -> PCChestPlacement.direction),
			((MapCodec)BlockPredicate.BASE_CODEC.fieldOf("direction_target_condition")).forGetter(PCChestPlacement -> PCChestPlacement.directionPredicate),
			((MapCodec)BlockPredicate.BASE_CODEC.fieldOf("target_condition")).forGetter(PCChestPlacement -> PCChestPlacement.targetPredicate),
			((MapCodec)Codec.intRange(1, 32).fieldOf("max_steps"))
					.forGetter(PCChestPlacement -> PCChestPlacement.maxSteps)).apply((Applicative<PCChestPlacement, ?>)instance, PCChestPlacement::new));


	private PCChestPlacement(Direction direction, BlockPredicate directionPredicate, BlockPredicate targetPredicate, int maxSteps) {
		this.direction = direction;
		this.targetPredicate = targetPredicate;
		this.directionPredicate = directionPredicate;
		this.maxSteps = maxSteps;
	}

	public static PCChestPlacement of(Direction direction, BlockPredicate directionPredicate, BlockPredicate targetPredicate, int maxSteps) {
		return new PCChestPlacement(direction, directionPredicate, targetPredicate, maxSteps);
	}

	@Override
	public Stream<BlockPos> getPositions (FeaturePlacementContext context, Random random, BlockPos pos) {
		BlockPos.Mutable mutableTarget = pos.mutableCopy();
		BlockPos.Mutable mutableDirection = pos.mutableCopy();
		mutableDirection.move(Direction.DOWN);
		StructureWorldAccess structureWorldAccess = context.getWorld();
		for (int i = 0; i < this.maxSteps; ++i) {
			if (this.targetPredicate.test(structureWorldAccess, mutableTarget) && this.directionPredicate.test(structureWorldAccess,mutableDirection)) {
				return Stream.of(mutableTarget);
			}
			mutableTarget.move(this.direction);
			mutableDirection.move(this.direction);
			if (structureWorldAccess.isOutOfHeightLimit(mutableTarget.getY())) {
				return Stream.of(new BlockPos[0]);
			}
		}
		if (this.targetPredicate.test(structureWorldAccess, mutableTarget) && this.directionPredicate.test(structureWorldAccess,mutableDirection)) {
			return Stream.of(mutableTarget);
		}
		return Stream.of(new BlockPos[0]);
	}

	@Override
	public PlacementModifierType<?> getType () {
		return PlacementModifierType.ENVIRONMENT_SCAN;
	}
}
