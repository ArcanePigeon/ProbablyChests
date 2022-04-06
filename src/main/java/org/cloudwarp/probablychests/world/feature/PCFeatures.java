package org.cloudwarp.probablychests.world.feature;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCFeatures {
	private static final BlockPredicate VALID_FLOOR = BlockPredicate.anyOf(BlockPredicate.matchingBlockTag(BlockTags.BASE_STONE_OVERWORLD),
			BlockPredicate.matchingBlockTag(BlockTags.BASE_STONE_NETHER),
			BlockPredicate.matchingBlockTag(BlockTags.NYLIUM),
			BlockPredicate.matchingBlockTag(BlockTags.SMALL_DRIPLEAF_PLACEABLE));
	//--------------------------------------------------------------------------
	private static final Feature<PCChestFeatureConfig> PC_CHEST = new PCChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> LUSH_CHEST =
			ConfiguredFeatures.register("lush_chest", PC_CHEST, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//---------------------------------------------------------------------------
	public static final RegistryEntry<PlacedFeature> LUSH_CHEST_PLACED = PlacedFeatures.register("lush_chest_placed",
			LUSH_CHEST, CountPlacementModifier.of(2), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
			PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), VALID_FLOOR, BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG));
	private static final Feature<PCPotFeatureConfig> PC_POT = new PCPotFeature(PCPotFeatureConfig.CODEC);
	//---------------------------------------------------------------------------
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> NORMAL_POT =
			ConfiguredFeatures.register("normal_pot", PC_POT, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	public static final RegistryEntry<PlacedFeature> NORMAL_POT_PLACED = PlacedFeatures.register("normal_pot_placed",
			NORMAL_POT, CountPlacementModifier.of(12), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
			PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), VALID_FLOOR, BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG));

	//,BiomePlacementModifier.of()
	//RarityFilterPlacementModifier.of(4)
	//EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR, 12),
	//---------------------------------------------------------------------------

	public static void init () {
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_chest"), PC_CHEST);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_pot"), PC_POT);
	}
}
