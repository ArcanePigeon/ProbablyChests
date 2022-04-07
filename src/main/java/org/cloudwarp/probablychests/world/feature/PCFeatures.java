package org.cloudwarp.probablychests.world.feature;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.Config;

public class PCFeatures {

	//--------------------------------------------------------------------------
	private static final Feature<PCChestFeatureConfig> LUSH_CHEST_FEATURE = new LushChestFeature(PCChestFeatureConfig.CODEC);
	//---------------------------------------------------------------------------
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> LUSH_CHEST =
			ConfiguredFeatures.register("lush_chest_feature", LUSH_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCChestFeatureConfig> NORMAL_CHEST_FEATURE = new NormalChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> NORMAL_CHEST =
			ConfiguredFeatures.register("normal_chest_feature", NORMAL_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCChestFeatureConfig> ROCKY_CHEST_FEATURE = new RockyChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> ROCKY_CHEST =
			ConfiguredFeatures.register("rocky_chest_feature", ROCKY_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//------------------------
	private static final Feature<PCPotFeatureConfig> NORMAL_POT_FEATURE = new NormalPotFeature(PCPotFeatureConfig.CODEC);
	//---------------------------------------------------------------------------
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> NORMAL_POT =
			ConfiguredFeatures.register("normal_pot", NORMAL_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> LUSH_POT_FEATURE = new LushPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> LUSH_POT =
			ConfiguredFeatures.register("lush_pot", LUSH_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> ROCKY_POT_FEATURE = new RockyPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> ROCKY_POT =
			ConfiguredFeatures.register("rocky_pot", ROCKY_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	private static final Feature<PCPotFeatureConfig> NETHER_POT_FEATURE = new NetherPotFeature(PCPotFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCPotFeatureConfig, ?>> Nether_POT =
			ConfiguredFeatures.register("nether_pot", NETHER_POT_FEATURE, new PCPotFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//---------------------------------------------------------------------------
	public static RegistryEntry<PlacedFeature> LUSH_CHEST_PLACED;
	//------------------------
	public static RegistryEntry<PlacedFeature> NORMAL_CHEST_PLACED;
	//------------------------
	public static RegistryEntry<PlacedFeature> ROCKY_CHEST_PLACED;
	//------------------------
	public static RegistryEntry<PlacedFeature> NORMAL_POT_PLACED;
	public static RegistryEntry<PlacedFeature> LUSH_POT_PLACED;
	public static RegistryEntry<PlacedFeature> ROCKY_POT_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_POT_PLACED;

	//,BiomePlacementModifier.of()
	//RarityFilterPlacementModifier.of(4)
	//EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR, 12),
	//---------------------------------------------------------------------------

	public static void init () {
		Config config = ProbablyChests.config;
		int chestFrequency = config.getChestFrequency();
		int potFrequency = config.getPotFrequency();
		//-----------------------------------
		LUSH_CHEST_PLACED = PlacedFeatures.register("lush_chest_placed",
				LUSH_CHEST, CountPlacementModifier.of(chestFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		NORMAL_CHEST_PLACED = PlacedFeatures.register("normal_chest_placed",
				NORMAL_CHEST, CountPlacementModifier.of(chestFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		ROCKY_CHEST_PLACED = PlacedFeatures.register("rocky_chest_placed",
				ROCKY_CHEST, CountPlacementModifier.of(chestFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		//------------------------------------------------
		NORMAL_POT_PLACED = PlacedFeatures.register("normal_pot_placed",
				NORMAL_POT, CountPlacementModifier.of(potFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		LUSH_POT_PLACED = PlacedFeatures.register("lush_pot_placed",
				LUSH_POT, CountPlacementModifier.of(potFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		ROCKY_POT_PLACED = PlacedFeatures.register("rocky_pot_placed",
				ROCKY_POT, CountPlacementModifier.of(potFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		NETHER_POT_PLACED = PlacedFeatures.register("nether_pot_placed",
				Nether_POT, CountPlacementModifier.of(potFrequency), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR, 8, Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of());
		//-------------------
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_lush_chest"), LUSH_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_normal_chest"), NORMAL_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_rocky_chest"), ROCKY_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_normal_pot"), NORMAL_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_lush_pot"), LUSH_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_rocky_pot"), ROCKY_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_nether_pot"), NETHER_POT_FEATURE);
	}
}
