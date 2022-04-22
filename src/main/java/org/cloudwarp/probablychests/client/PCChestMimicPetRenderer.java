package org.cloudwarp.probablychests.client;


import net.minecraft.client.render.entity.EntityRendererFactory;
import org.cloudwarp.probablychests.client.entity.model.PCChestMimicModel;
import org.cloudwarp.probablychests.client.entity.model.PCChestMimicPetModel;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PCChestMimicPetRenderer extends GeoEntityRenderer<PCChestMimicPet> {

	public PCChestMimicPetRenderer (EntityRendererFactory.Context renderManager, String texture) {
		super(renderManager, new PCChestMimicPetModel(texture));
	}
}
