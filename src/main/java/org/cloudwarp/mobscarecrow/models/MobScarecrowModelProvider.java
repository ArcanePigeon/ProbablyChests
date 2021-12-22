package org.cloudwarp.mobscarecrow.models;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class MobScarecrowModelProvider implements ModelResourceProvider {
    public static final Identifier SCARECROW_MODEL = new Identifier("mobscarecrow", "scarecrow");
    public static final Identifier CREEPER_SCARECROW_MODEL = new Identifier("mobscarecrow", "creeper_scarecrow");
    public static final Identifier SKELETON_SCARECROW_MODEL = new Identifier("mobscarecrow", "skeleton_scarecrow");
    public static final Identifier SPIDER_SCARECROW_MODEL = new Identifier("mobscarecrow", "spider_scarecrow");
    public static final Identifier ZOMBIE_SCARECROW_MODEL = new Identifier("mobscarecrow", "zombie_scarecrow");
    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(SCARECROW_MODEL) || identifier.equals(CREEPER_SCARECROW_MODEL) || identifier.equals(SKELETON_SCARECROW_MODEL) || identifier.equals(SPIDER_SCARECROW_MODEL) || identifier.equals( ZOMBIE_SCARECROW_MODEL)) {
            return new MobScarecrowModel(identifier);
        } else {
            return null;
        }
    }
}
