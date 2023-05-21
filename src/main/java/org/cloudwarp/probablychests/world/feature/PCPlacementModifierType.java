package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCPlacementModifierType<P extends PlacementModifier> {
	public static PlacementModifierType<PCGroundPlacementModifier> CHEST_SCAN;
	public static PlacementModifierType<PCRarityFilterPlacementModifier> PC_RARITY;
	public static PlacementModifierType<PCSolidGroundPlacementModifier> SOLID_CHECK;

	private static <P extends PlacementModifier> PlacementModifierType<P> register (Identifier id, Codec<P> codec) {
		return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, id, () -> codec);
	}

	public static void init () {
		CHEST_SCAN = register(ProbablyChests.id("chest_scan"), PCGroundPlacementModifier.MODIFIER_CODEC);
		PC_RARITY = register(ProbablyChests.id("pc_rarity"), PCRarityFilterPlacementModifier.MODIFIER_CODEC);
		SOLID_CHECK = register(ProbablyChests.id("sold_check"), PCSolidGroundPlacementModifier.MODIFIER_CODEC);
	}
}
