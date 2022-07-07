package org.cloudwarp.probablychests;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.probablychests.registry.*;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.utils.PCEventHandler;
import org.cloudwarp.probablychests.world.feature.PCFeatures;
import org.cloudwarp.probablychests.world.feature.PCPlacementModifierType;
import org.cloudwarp.probablychests.world.gen.PCWorldGen;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

public class ProbablyChests implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "probablychests";
	public static final ItemGroup PROBABLY_CHESTS_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, MOD_ID), () -> new ItemStack(PCBlocks.NORMAL_CHEST));
	public static ConfigHolder<PCConfig> configHolder;
	public static PCConfig loadedConfig;

	public static Identifier id (String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize () {
		LOGGER.info("[Probably-Chests] is initializing.");
		AutoConfig.register(PCConfig.class, Toml4jConfigSerializer::new);
		configHolder = AutoConfig.getConfigHolder(PCConfig.class);
		loadedConfig = getConfig();
		PCEventHandler.registerEvents();
		GeckoLibMod.DISABLE_IN_DEV = true;
		GeckoLib.initialize();
		PCLootTables.init();
		PCBlockEntities.init();
		PCBlocks.init();
		PCEntities.init();
		PCItems.init();
		PCPlacementModifierType.init();
		PCFeatures.init();
		PCEntitySpawns.init();
		PCScreenHandlerType.registerScreenHandlers();
		PCWorldGen.generatePCWorldGen();
		LOGGER.info("[Probably-Chests] has successfully been initialized.");
		LOGGER.info("[Probably-Chests] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}
	public static PCConfig getConfig () {
		return configHolder.getConfig();
	}
	public static NbtCompound configToNBT(){
		PCConfig config = getConfig();
		NbtCompound nbt = new NbtCompound();
		nbt.putFloat("pot_spawn_chance", config.worldGen.potSpawnChance);
		nbt.putFloat("chest_spawn_chance", config.worldGen.chestSpawnChance);
		nbt.putFloat("surface_chest_spawn_chance", config.worldGen.surfaceChestSpawnChance);
		nbt.putFloat("secret_mimic_chance", config.worldGen.secretMimicChance);
		nbt.putBoolean("easier_mimics", config.mimicSettings.easierMimics);
		nbt.putBoolean("spawn_natural_mimics", config.mimicSettings.spawnNaturalMimics);
		nbt.putFloat("natural_mimic_spawn_rate", config.mimicSettings.naturalMimicSpawnRate);
		nbt.putBoolean("allow_pet_mimics", config.mimicSettings.allowPetMimics);
		return nbt;
	}
	public static PCConfig nbtToConfig(NbtCompound nbt){
		PCConfig config = new PCConfig();
		if(nbt == null){
			return config;
		}
		config.worldGen.potSpawnChance = nbt.getFloat("pot_spawn_chance");
		config.worldGen.chestSpawnChance = nbt.getFloat("chest_spawn_chance");
		config.worldGen.surfaceChestSpawnChance = nbt.getFloat("surface_chest_spawn_chance");
		config.worldGen.secretMimicChance = nbt.getFloat("secret_mimic_chance");
		config.mimicSettings.easierMimics = nbt.getBoolean("easier_mimics");
		config.mimicSettings.spawnNaturalMimics = nbt.getBoolean("spawn_natural_mimics");
		config.mimicSettings.naturalMimicSpawnRate = nbt.getFloat("natural_mimic_spawn_rate");
		config.mimicSettings.allowPetMimics = nbt.getBoolean("allow_pet_mimics");
		return config;
	}
}
