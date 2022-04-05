package org.cloudwarp.probablychests.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Environment(EnvType.CLIENT)
public class ProbablyChestsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		// The IDE is lying to you the type casting is necessary
		ScreenRegistry.<PCScreenHandler, CottonInventoryScreen<PCScreenHandler>>register(PCScreenHandlerType.LUSH_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));

		BlockEntityRendererRegistry.register(PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCBlockEntityRenderer());

	}
}
