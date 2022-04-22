package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.Random;

public class PCRarityFilterPlacementModifier extends AbstractConditionalPlacementModifier {
	public static final Codec<PCRarityFilterPlacementModifier> MODIFIER_CODEC;

	static {
		MODIFIER_CODEC = Codecs.POSITIVE_FLOAT.fieldOf("chance").xmap(PCRarityFilterPlacementModifier::new, (PCRarityFilterPlacementModifier) -> {
			return PCRarityFilterPlacementModifier.chance;
		}).codec();
	}

	private final float chance;

	private PCRarityFilterPlacementModifier (float chance) {
		this.chance = chance;
	}

	public static PCRarityFilterPlacementModifier of (float chance) {
		return new PCRarityFilterPlacementModifier(chance);
	}

	protected boolean shouldPlace (FeaturePlacementContext context, Random random, BlockPos pos) {
		return random.nextFloat() < this.chance;
	}

	public PlacementModifierType<?> getType () {
		return PCPlacementModifierType.PC_RARITY;
	}
}
