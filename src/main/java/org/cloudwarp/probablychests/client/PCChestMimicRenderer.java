package org.cloudwarp.probablychests.client;


import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.client.entity.model.PCChestBlockModel;
import org.cloudwarp.probablychests.client.entity.model.PCChestMimicModel;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PCChestMimicRenderer extends GeoEntityRenderer<PCChestMimic> {

	public PCChestMimicRenderer(EntityRendererFactory.Context renderManager, String texture) {
		super(renderManager, new PCChestMimicModel(texture));
	}
}
