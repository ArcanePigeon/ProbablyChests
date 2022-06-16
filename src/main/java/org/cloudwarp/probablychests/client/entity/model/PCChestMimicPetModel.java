package org.cloudwarp.probablychests.client.entity.model;

import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PCChestMimicPetModel extends AnimatedGeoModel<PCChestMimicPet> {

	private static final Identifier MODEL_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "geo/pc_chest_mimic.json");
	private static final Identifier ANIMATION_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "animations/pc_chest_mimic.animation.json");
	private final Identifier TEXTURE_IDENTIFIER;

	public PCChestMimicPetModel (String texture) {
		TEXTURE_IDENTIFIER = new Identifier(ProbablyChests.MOD_ID, "textures/entity/" + texture + ".png");
	}

	@Override
	public Identifier getTextureResource (PCChestMimicPet entity) {
		return TEXTURE_IDENTIFIER;
	}

	@Override
	public Identifier getModelResource (PCChestMimicPet entity) {
		return MODEL_IDENTIFIER;
	}

	@Override
	public Identifier getAnimationResource (PCChestMimicPet entity) {
		return ANIMATION_IDENTIFIER;
	}
}
