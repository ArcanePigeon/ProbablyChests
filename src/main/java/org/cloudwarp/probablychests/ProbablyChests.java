package org.cloudwarp.probablychests;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.utils.Config;
import org.cloudwarp.probablychests.world.feature.PCFeatures;
import org.cloudwarp.probablychests.world.feature.PCPlacementModifierType;
import org.cloudwarp.probablychests.world.gen.PCWorldGen;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

public class ProbablyChests implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "probablychests";
	public static final ItemGroup PROBABLY_CHESTS_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, MOD_ID), () -> new ItemStack(PCBlocks.LUSH_CHEST));
	public static Config config;

	public static Identifier id (String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize () {
		LOGGER.info("[Probably-Chests] is initializing.");
		GeckoLibMod.DISABLE_IN_DEV = true;
		Config.getInstance().loadConfig();
		config = Config.getInstance();
		GeckoLib.initialize();
		PCPlacementModifierType.init();
		PCFeatures.init();
		PCBlockEntities.init();
		PCBlocks.init();
		PCScreenHandlerType.registerScreenHandlers();
		PCWorldGen.generatePCWorldGen();
		LOGGER.info("[Probably-Chests] has successfully been initialized.");
		LOGGER.info("[Probably-Chests] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}

}
