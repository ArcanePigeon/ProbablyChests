package org.cloudwarp.mobscarecrow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.RenderLayer;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

public class MobScarecrowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new MobScarecrowModelProvider());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MOB_SCARECROW, RenderLayer.getCutout());
        /* Other client-specific initialization */
    }
}
