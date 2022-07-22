package org.cloudwarp.probablychests.client.entity.model;

import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PCChestBlockModel extends AnimatedGeoModel<PCBaseChestBlockEntity> {

	private static final Identifier MODEL_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "geo/pc_chest_block.json");
	private static final Identifier ANIMATION_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "animations/pc_chest_block.animation.json");
	private Identifier TEXTURE_IDENTIFIER;

	public PCChestBlockModel (String texture) {
		TEXTURE_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "textures/block/" + texture + ".png");
	}

	@Override
	public Identifier getTextureResource (PCBaseChestBlockEntity entity) {
		return TEXTURE_IDENTIFIER;
	}

	@Override
	public Identifier getModelResource (PCBaseChestBlockEntity entity) {
		return MODEL_IDENTIFIER;
	}

	@Override
	public Identifier getAnimationResource (PCBaseChestBlockEntity entity) {
		return ANIMATION_IDENTIFIER;
	}
}