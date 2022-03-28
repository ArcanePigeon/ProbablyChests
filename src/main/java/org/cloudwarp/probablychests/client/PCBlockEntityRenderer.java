package org.cloudwarp.probablychests.client;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.block.PCChestBlock;

@Environment(EnvType.CLIENT)
public class PCBlockEntityRenderer <T extends ChestBlockEntity> extends ChestBlockEntityRenderer<T> {

	private final ModelPart chestLid;
	private final ModelPart chestBottom;
	private final ModelPart chestLock;

	public PCBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		super(context);

		ModelPart modelPart = context.getLayerModelPart(EntityModelLayers.CHEST);
		this.chestLid = modelPart.getChild("lid");
		this.chestBottom = modelPart.getChild("bottom");
		this.chestLock = modelPart.getChild("lock");
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();

		BlockState blockState = world != null ? entity.getCachedState() : (BlockState) Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		Block block = blockState.getBlock();

		if (block instanceof PCChestBlock) {
			PCChestBlock chest = (PCChestBlock)block;

			matrices.push();
			float rotation = ((Direction)blockState.get(ChestBlock.FACING)).asRotation();
			matrices.translate(0.5D, 0.5D, 0.5D);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rotation));
			matrices.translate(-0.5D, -0.5D, -0.5D);

			DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> properties;
			if (world == null) {
				properties = DoubleBlockProperties.PropertyRetriever::getFallback;
			} else {
				properties = chest.getBlockEntitySource(blockState, world, entity.getPos(), true);
			}

			float g = ((Float2FloatFunction)properties.apply(PCChestBlock.getAnimationProgressRetriever((ChestAnimationProgress)entity))).get(tickDelta);
			g = 1.0F - g;
			g = 1.0F - g * g * g;
			int i = ((Int2IntFunction)properties.apply(new LightmapCoordinatesRetriever())).applyAsInt(light);

			SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, chest.getType().texture);
			VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

			handleModelRender(matrices, vertexConsumer, this.chestLid, this.chestLock, this.chestBottom, g, i, overlay);

			matrices.pop();
		}
	}
	private static void handleModelRender(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay) {
		lid.pitch = -openFactor * 1.5707964F;
		latch.pitch = lid.pitch;
		lid.render(matrices, vertices, light, overlay);
		latch.render(matrices, vertices, light, overlay);
		base.render(matrices, vertices, light, overlay);
	}

}
