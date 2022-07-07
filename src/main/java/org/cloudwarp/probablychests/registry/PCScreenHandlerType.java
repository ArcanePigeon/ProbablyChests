package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public class PCScreenHandlerType {
	public static ScreenHandlerType<PCScreenHandler> PC_CHEST;
	public static ScreenHandlerType<PCMimicScreenHandler> PC_CHEST_MIMIC;

	public static void registerScreenHandlers () {
		PC_CHEST = Registry.register(Registry.SCREEN_HANDLER,ProbablyChests.id("pc_chest"), new ScreenHandlerType<>(PCScreenHandler::new));
		PC_CHEST_MIMIC = Registry.register(Registry.SCREEN_HANDLER,ProbablyChests.id("pc_chest_mimic"), new ScreenHandlerType<>(PCMimicScreenHandler::new));

	}
}
