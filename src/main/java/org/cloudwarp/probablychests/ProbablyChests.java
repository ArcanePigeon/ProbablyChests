package org.cloudwarp.probablychests;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCBlockEntityTypes;
import org.cloudwarp.probablychests.registry.PCItems;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.utils.Config;

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
		Config.getInstance().loadConfig();
		config = Config.getInstance();
		PCItems.registerItems();
		PCBlocks.init();
		PCBlockEntityTypes.init();
		PCScreenHandlerType.registerScreenHandlers();
		LOGGER.info("[Probably-Chests] has successfully been initialized.");
		LOGGER.info("[Probably-Chests] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}

}
