package org.cloudwarp.probablychests.registry;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;

public class PCScreenHandlerType {
	public static ScreenHandlerType<PCChestScreenHandler> PC_CHEST;
	public static ScreenHandlerType<PCMimicScreenHandler> PC_CHEST_MIMIC;

	public static void registerScreenHandlers () {
		PC_CHEST = Registry.register(Registry.SCREEN_HANDLER,ProbablyChests.id("pc_chest_screen_handler"), new ScreenHandlerType<>(PCChestScreenHandler::new));
		PC_CHEST_MIMIC = Registry.register(Registry.SCREEN_HANDLER,ProbablyChests.id("pc_chest_mimic_screen_handler"), new ScreenHandlerType<>(PCMimicScreenHandler::new));
	}
}
