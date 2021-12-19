package org.cloudwarp.mobscarecrow;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class MobScarecrowBlockTags {
    public static final Tag.Identified<Block> MOB_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "scarecrow"));
}
