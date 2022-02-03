package org.cloudwarp.mobscarecrow;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;
import org.cloudwarp.mobscarecrow.registry.ModItems;
import org.cloudwarp.mobscarecrow.registry.ModSounds;

public class MobScarecrow implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize () {
		LOGGER.info("[Mob-Scarecrow] is initializing.");
		ModSounds.registerSounds();
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModBlocks.RegisterBlockEntities();
		LOGGER.info("[Mob-Scarecrow] has successfully been initialized.");
		LOGGER.info("[Mob-Scarecrow] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}

}
