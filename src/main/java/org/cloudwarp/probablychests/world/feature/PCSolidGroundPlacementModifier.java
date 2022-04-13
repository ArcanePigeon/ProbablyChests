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

public class PCSolidGroundPlacementModifier extends PlacementModifier {


	public static final Codec<PCSolidGroundPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
					(BlockPredicate.BASE_CODEC.fieldOf("target_condition")).forGetter(PCSolidGroundPlacementModifier -> PCSolidGroundPlacementModifier.targetPredicate)
					)
			.apply(instance, PCSolidGroundPlacementModifier::new));
	//---------------------------------------------------

	private final BlockPredicate targetPredicate;



	private PCSolidGroundPlacementModifier (BlockPredicate targetPredicate) {

		this.targetPredicate = targetPredicate;

	}

	public static PCSolidGroundPlacementModifier of (BlockPredicate targetPredicate) {
		return new PCSolidGroundPlacementModifier(targetPredicate);
	}

	@Override
	public Stream<BlockPos> getPositions (FeaturePlacementContext context, Random random, BlockPos pos) {
		BlockPos.Mutable mutableTarget = pos.mutableCopy();
		mutableTarget.move(Direction.DOWN);
		StructureWorldAccess structureWorldAccess = context.getWorld();
		if (this.targetPredicate.test(structureWorldAccess, mutableTarget)) {
			return Stream.of(pos);
		}
		return Stream.of(new BlockPos[0]);
	}

	@Override
	public PlacementModifierType<?> getType () {
		return PCPlacementModifierType.SOLID_CHECK;
	}
}
