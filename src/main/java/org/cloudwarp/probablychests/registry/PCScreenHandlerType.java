package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public class PCScreenHandlerType {
	public static ScreenHandlerType<PCScreenHandler> PC_CHEST;
	public static ScreenHandlerType<PCMimicScreenHandler> PC_CHEST_MIMIC;

	public static void registerScreenHandlers () {
		PC_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(ProbablyChests.MOD_ID, "pc_chest"),
				(syncId, inventory) -> new PCScreenHandler(PC_CHEST, PCChestTypes.NORMAL, syncId, inventory, ScreenHandlerContext.EMPTY));
		PC_CHEST_MIMIC = ScreenHandlerRegistry.registerSimple(new Identifier(ProbablyChests.MOD_ID, "pc_chest_mimic"),
				(syncId, inventory) -> new PCMimicScreenHandler(PC_CHEST_MIMIC, PCChestTypes.NORMAL, syncId, inventory, new SimpleInventory()));

	}
}
