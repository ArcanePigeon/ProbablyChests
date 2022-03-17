package org.cloudwarp.mobscarecrow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

@Environment(EnvType.CLIENT)
public class MobScarecrowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MOB_SCARECROW, RenderLayer.getCutout());
	}
}
