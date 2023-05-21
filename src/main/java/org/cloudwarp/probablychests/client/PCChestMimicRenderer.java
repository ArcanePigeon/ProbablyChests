package org.cloudwarp.probablychests.client;


import net.minecraft.client.render.entity.EntityRendererFactory;
import org.cloudwarp.probablychests.client.entity.model.PCChestMimicModel;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PCChestMimicRenderer extends GeoEntityRenderer<PCChestMimic> {

	public PCChestMimicRenderer (EntityRendererFactory.Context renderManager, String texture) {
		super(renderManager, new PCChestMimicModel(texture));
	}
}
