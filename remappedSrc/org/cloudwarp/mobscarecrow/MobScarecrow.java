package org.cloudwarp.mobscarecrow;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudwarp.mobscarecrow.networking.MobScarecrowNetworking;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;
import org.cloudwarp.mobscarecrow.registry.ModGameRules;
import org.cloudwarp.mobscarecrow.registry.ModItems;
import org.cloudwarp.mobscarecrow.registry.ModSounds;

public class MobScarecrow implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "mobscarecrow";
	public static int mobScarecrowRadius;

	public static Identifier id (String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize () {
		LOGGER.info("[Mob-Scarecrow] is initializing.");
		ModGameRules.init();
		MobScarecrowNetworking.init();
		ModSounds.registerSounds();
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModBlocks.RegisterBlockEntities();
		LOGGER.info("[Mob-Scarecrow] has successfully been initialized.");
		LOGGER.info("[Mob-Scarecrow] if you have any issues or questions feel free to join my Discord: https://discord.gg/fvcFxTg6sB");
	}

}
