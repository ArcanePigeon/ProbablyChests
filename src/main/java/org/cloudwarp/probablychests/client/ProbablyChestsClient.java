package org.cloudwarp.probablychests.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.cloudwarp.probablychests.registry.PCBlockEntityTypes;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

@Environment(EnvType.CLIENT)
public class ProbablyChestsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		ScreenRegistry.<PCScreenHandler, CottonInventoryScreen<PCScreenHandler>>register(PCScreenHandlerType.LUSH_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));

		BlockEntityRendererRegistry.INSTANCE.register(PCBlockEntityTypes.PC_CHEST_BLOCK_ENTITY_TYPE, PCBlockEntityRenderer::new);
	}
}
