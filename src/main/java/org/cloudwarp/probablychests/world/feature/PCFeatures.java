package org.cloudwarp.probablychests.world.feature;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;

import java.util.List;

public class PCFeatures {

	//--------------------------------------------------------------------------
	private static final Feature<DefaultFeatureConfig> SURFACE_CHEST_FEATURE = new SurfaceChestFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> SURFACE_CHEST =
			ConfiguredFeatures.register("surface_chest_feature", SURFACE_CHEST_FEATURE, new DefaultFeatureConfig());

	private static final Feature<DefaultFeatureConfig> UNDERGROUND_CHEST_FEATURE = new UndergroundChestFeature(DefaultFeatureConfig.CODEC);
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> UNDERGROUND_CHEST =
			ConfiguredFeatures.register("underground_chest_feature", UNDERGROUND_CHEST_FEATURE, new DefaultFeatureConfig());
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> NETHER_CHEST =
			ConfiguredFeatures.register("nether_chest_feature", UNDERGROUND_CHEST_FEATURE, new DefaultFeatureConfig());
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
	public static RegistryEntry<PlacedFeature> SURFACE_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> UNDERGROUND_CHEST_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_CHEST_PLACED;
	//------------------------
	public static RegistryEntry<PlacedFeature> NORMAL_POT_PLACED;
	public static RegistryEntry<PlacedFeature> LUSH_POT_PLACED;
	public static RegistryEntry<PlacedFeature> ROCKY_POT_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_POT_PLACED;

	public static void init () {
		PCConfig config = ProbablyChests.loadedConfig;
		float chestSpawnChance = config.worldGen.chestSpawnChance;
		float potSpawnChance = config.worldGen.potSpawnChance;
		float surfaceChestSpawnChance = config.worldGen.surfaceChestSpawnChance;
		//-----------------------------------
		SURFACE_CHEST_PLACED = register(ProbablyChests.id("surface_chest_placed"),
				SURFACE_CHEST,
				PCRarityFilterPlacementModifier.of(surfaceChestSpawnChance * 0.1F),
				SquarePlacementModifier.of(),
				PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP
		);
		UNDERGROUND_CHEST_PLACED = register(ProbablyChests.id("underground_chest_placed"),
				UNDERGROUND_CHEST,
				CountPlacementModifier.of(2),
				SquarePlacementModifier.of(),
				PCRarityFilterPlacementModifier.of(chestSpawnChance),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(64)),
				EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 32)
		);
		NETHER_CHEST_PLACED = register(ProbablyChests.id("nether_chest_placed"),
				NETHER_CHEST,
				CountPlacementModifier.of(2),
				SquarePlacementModifier.of(),
				PCRarityFilterPlacementModifier.of(chestSpawnChance * 0.75F),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.belowTop(6)),
				EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 12)
		);
		//------------------------------------------------
		NORMAL_POT_PLACED = register(ProbablyChests.id("normal_pot_placed"),
				NORMAL_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		LUSH_POT_PLACED = register(ProbablyChests.id("lush_pot_placed"),
				LUSH_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		ROCKY_POT_PLACED = register(ProbablyChests.id("rocky_pot_placed"),
				ROCKY_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		NETHER_POT_PLACED = register(ProbablyChests.id("nether_pot_placed"),
				Nether_POT, PCRarityFilterPlacementModifier.of(potSpawnChance), CountPlacementModifier.of(8), SquarePlacementModifier.of(),
				PCGroundPlacementModifier.of(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.IS_AIR,
						20, Heightmap.Type.WORLD_SURFACE_WG, 300),
				BiomePlacementModifier.of());
		//-------------------
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_surface_chest"), SURFACE_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_underground_chest"), UNDERGROUND_CHEST_FEATURE);
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_normal_pot"), NORMAL_POT_FEATURE);
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_lush_pot"), LUSH_POT_FEATURE);
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_rocky_pot"), ROCKY_POT_FEATURE);
		Registry.register(Registry.FEATURE, ProbablyChests.id("pc_nether_pot"), NETHER_POT_FEATURE);
	}

	public static RegistryEntry<PlacedFeature> register (Identifier id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, List<PlacementModifier> modifiers) {
		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(RegistryEntry.upcast(registryEntry), List.copyOf(modifiers)));
	}

	public static RegistryEntry<PlacedFeature> register (Identifier id, RegistryEntry<? extends ConfiguredFeature<?, ?>> registryEntry, PlacementModifier... modifiers) {
		return register(id, registryEntry, List.of(modifiers));
	}
}
