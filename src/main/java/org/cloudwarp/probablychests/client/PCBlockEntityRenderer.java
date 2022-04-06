package org.cloudwarp.probablychests.client;


import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.client.entity.model.PCChestBlockModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PCBlockEntityRenderer extends GeoBlockRenderer<PCChestBlockEntity> {

	public PCBlockEntityRenderer (String texture) {
		super(new PCChestBlockModel(texture));
	}

	@Override
	public RenderLayer getRenderType (PCChestBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
	}
}
