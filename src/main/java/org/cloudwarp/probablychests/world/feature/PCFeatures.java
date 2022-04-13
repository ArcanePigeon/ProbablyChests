package org.cloudwarp.probablychests.world.feature;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.Config;

public class PCFeatures {

	//--------------------------------------------------------------------------
	private static final Feature<PCChestFeatureConfig> LUSH_CHEST_FEATURE = new LushChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> LUSH_CHEST =
			ConfiguredFeatures.register("lush_chest_feature", LUSH_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> LUSH_CHEST_SURFACE_FEATURE = new LushChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> LUSH_CHEST_SURFACE =
			ConfiguredFeatures.register("lush_chest_surface_feature", LUSH_CHEST_SURFACE_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> NORMAL_CHEST_FEATURE = new NormalChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> NORMAL_CHEST =
			ConfiguredFeatures.register("normal_chest_feature", NORMAL_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> NORMAL_CHEST_SURFACE_FEATURE = new NormalChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> NORMAL_CHEST_SURFACE =
			ConfiguredFeatures.register("normal_chest_surface_feature", NORMAL_CHEST_SURFACE_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> ROCKY_CHEST_FEATURE = new RockyChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> ROCKY_CHEST =
			ConfiguredFeatures.register("rocky_chest_feature", ROCKY_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> ROCKY_CHEST_SURFACE_FEATURE = new RockyChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> ROCKY_CHEST_SURFACE =
			ConfiguredFeatures.register("rocky_chest_surface_feature", ROCKY_CHEST_SURFACE_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> STONE_CHEST_FEATURE = new StoneChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> STONE_CHEST =
			ConfiguredFeatures.register("stone_chest_feature", STONE_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
	//--------------------------------------------
	private static final Feature<PCChestFeatureConfig> GOLD_CHEST_FEATURE = new GoldChestFeature(PCChestFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<PCChestFeatureConfig, ?>> GOLD_CHEST =
			ConfiguredFeatures.register("gold_chest_feature", GOLD_CHEST_FEATURE, new PCChestFeatureConfig(UniformFloatProvider.create(0f, 1f)));
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
	public static RegistryEntry<PlacedFeature> LUSH_CHEST_PLACED_SURFACE;
	public static RegistryEntry<PlacedFeature> NORMAL_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> NORMAL_CHEST_PLACED_SURFACE;
	public static RegistryEntry<PlacedFeature> ROCKY_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> ROCKY_CHEST_PLACED_SURFACE;
	public static RegistryEntry<PlacedFeature> STONE_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> GOLD_CHEST_PLACED;
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
		float chestRarity = config.getChestSpawnChance();
		float potRarity = config.getPotSpawnChance();
		float surfaceChestRarity = config.getSurfaceChestSpawnChance();
		//-----------------------------------
		LUSH_CHEST_PLACED = PlacedFeatures.register("lush_chest_placed",
				LUSH_CHEST, PCRarityFilterPlacementModifier.of(chestRarity),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		LUSH_CHEST_PLACED_SURFACE = PlacedFeatures.register("lush_chest_placed_surface",
				LUSH_CHEST_SURFACE, SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
				PCRarityFilterPlacementModifier.of(surfaceChestRarity), PCSolidGroundPlacementModifier.of(BlockPredicate.hasSturdyFace(Direction.UP)),
				BiomePlacementModifier.of());
		//------------------------------------------------
		NORMAL_CHEST_PLACED = PlacedFeatures.register("normal_chest_placed",
				NORMAL_CHEST, PCRarityFilterPlacementModifier.of(chestRarity),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		NORMAL_CHEST_PLACED_SURFACE = PlacedFeatures.register("normal_chest_placed_surface",
				NORMAL_CHEST_SURFACE,  SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
				PCRarityFilterPlacementModifier.of(surfaceChestRarity), PCSolidGroundPlacementModifier.of(BlockPredicate.hasSturdyFace(Direction.UP)),
				BiomePlacementModifier.of());
		//------------------------------------------------
		ROCKY_CHEST_PLACED = PlacedFeatures.register("rocky_chest_placed",
				ROCKY_CHEST, PCRarityFilterPlacementModifier.of(chestRarity),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		ROCKY_CHEST_PLACED_SURFACE = PlacedFeatures.register("rocky_chest_placed_surface",
				ROCKY_CHEST_SURFACE,  SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
				PCRarityFilterPlacementModifier.of(surfaceChestRarity), PCSolidGroundPlacementModifier.of(BlockPredicate.hasSturdyFace(Direction.UP)),
				BiomePlacementModifier.of());
		//---------------------------------------------------------
		STONE_CHEST_PLACED = PlacedFeatures.register("stone_chest_placed",
				STONE_CHEST, PCRarityFilterPlacementModifier.of(chestRarity),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 0),
				BiomePlacementModifier.of());
		GOLD_CHEST_PLACED = PlacedFeatures.register("gold_chest_placed",
				GOLD_CHEST, PCRarityFilterPlacementModifier.of(chestRarity),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 0));
		//------------------------------------------------
		NORMAL_POT_PLACED = PlacedFeatures.register("normal_pot_placed",
				NORMAL_POT, PCRarityFilterPlacementModifier.of(potRarity), CountPlacementModifier.of(8),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		LUSH_POT_PLACED = PlacedFeatures.register("lush_pot_placed",
				LUSH_POT, PCRarityFilterPlacementModifier.of(potRarity), CountPlacementModifier.of(8),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		ROCKY_POT_PLACED = PlacedFeatures.register("rocky_pot_placed",
				ROCKY_POT, PCRarityFilterPlacementModifier.of(potRarity), CountPlacementModifier.of(8),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		NETHER_POT_PLACED = PlacedFeatures.register("nether_pot_placed",
				Nether_POT, PCRarityFilterPlacementModifier.of(potRarity), CountPlacementModifier.of(8),  SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		//-------------------
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_lush_chest"), LUSH_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_normal_chest"), NORMAL_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_rocky_chest"), ROCKY_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_lush_chest_surface"), LUSH_CHEST_SURFACE_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_normal_chest_surface"), NORMAL_CHEST_SURFACE_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_rocky_chest_surface"), ROCKY_CHEST_SURFACE_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_stone_chest"), STONE_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_gold_chest"), GOLD_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_normal_pot"), NORMAL_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_lush_pot"), LUSH_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_rocky_pot"), ROCKY_POT_FEATURE);
		Registry.register(Registry.FEATURE, new Identifier(ProbablyChests.MOD_ID, "pc_nether_pot"), NETHER_POT_FEATURE);
	}
}
