package org.cloudwarp.mobscarecrow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.RenderLayer;
import org.cloudwarp.mobscarecrow.MobScarecrow;
import org.cloudwarp.mobscarecrow.networking.MobScarecrowNetworking;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

@Environment(EnvType.CLIENT)
public class MobScarecrowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MOB_SCARECROW, RenderLayer.getCutout());
		ClientPlayNetworking.registerGlobalReceiver(MobScarecrowNetworking.MOB_SCARECROW_RADIUS, (client, handler, buf, responseSender) -> {
			int value = buf.readInt();
			client.execute(() -> MobScarecrow.mobScarecrowRadius = value);
		});
	}
}
