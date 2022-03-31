package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.enums.ChestType;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public class PCScreenHandlerType {
	public static ScreenHandlerType<PCScreenHandler> LUSH_CHEST;

	public static void registerScreenHandlers () {
		LUSH_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(ProbablyChests.MOD_ID, "lush_chest"), (syncId, inventory) -> new PCScreenHandler(LUSH_CHEST, PCChestTypes.LUSH, syncId, inventory, ScreenHandlerContext.EMPTY));
	}
}
