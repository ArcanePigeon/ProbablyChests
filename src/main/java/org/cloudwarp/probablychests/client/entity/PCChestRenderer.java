package org.cloudwarp.probablychests.client.entity;


import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.client.entity.model.PCChestBlockModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PCChestRenderer extends GeoBlockRenderer<PCBaseChestBlockEntity> {

	public PCChestRenderer (String texture) {
		super(new PCChestBlockModel(texture));
	}

	@Override
	public RenderLayer getRenderType (PCBaseChestBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityCutoutNoCull(getTextureResource(animatable), false);
	}
}