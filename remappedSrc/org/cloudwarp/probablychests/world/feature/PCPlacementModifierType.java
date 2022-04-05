package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class PCPlacementModifierType<P extends PlacementModifier> {
	public static PlacementModifierType<PCGroundPlacementModifier> CHEST_SCAN;

	private static <P extends PlacementModifier> PlacementModifierType<P> register (String id, Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, id, () -> codec);
	}

	public static void init () {
		CHEST_SCAN = register("chest_scan", PCGroundPlacementModifier.MODIFIER_CODEC);
	}
}
