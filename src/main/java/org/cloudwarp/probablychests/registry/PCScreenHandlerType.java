package org.cloudwarp.probablychests.registry;

import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;
import net.minecraft.registry.Registry;

public class PCScreenHandlerType {
	public static ScreenHandlerType<PCChestScreenHandler> PC_CHEST;
	public static ScreenHandlerType<PCMimicScreenHandler> PC_CHEST_MIMIC;

	public static void registerScreenHandlers () {
		PC_CHEST = Registry.register(Registries.SCREEN_HANDLER,ProbablyChests.id("pc_chest_screen_handler"), new ScreenHandlerType<>(PCChestScreenHandler::new, FeatureSet.empty()));
		PC_CHEST_MIMIC = Registry.register(Registries.SCREEN_HANDLER,ProbablyChests.id("pc_chest_mimic_screen_handler"), new ScreenHandlerType<>(PCMimicScreenHandler::new, FeatureSet.empty()));
	}
}
