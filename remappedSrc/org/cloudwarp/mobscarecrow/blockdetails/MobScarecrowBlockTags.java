package org.cloudwarp.mobscarecrow.blockdetails;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class MobScarecrowBlockTags {
	public static final Tag.Identified<Block> MOB_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "scarecrow"));
	public static final Tag.Identified<Block> CREEPER_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "creeper_scarecrow"));
	public static final Tag.Identified<Block> SKELETON_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "skeleton_scarecrow"));
	public static final Tag.Identified<Block> SPIDER_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "spider_scarecrow"));
	public static final Tag.Identified<Block> ZOMBIE_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "zombie_scarecrow"));
	public static final Tag.Identified<Block> TURTLE_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "turtle_scarecrow"));
	public static final Tag.Identified<Block> ENDERMITE_SCARECROW = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "endermite_scarecrow"));
	public static final Tag.Identified<Block> PLUSHIE = TagFactory.BLOCK.create(new Identifier("mobscarecrow", "plushie"));
}
