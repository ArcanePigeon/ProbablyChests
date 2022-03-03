package org.cloudwarp.mobscarecrow.blockdetails;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class MobScarecrowBlockTags {
	public static final TagKey<Block> MOB_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "scarecrow"));
	public static final TagKey<Block> CREEPER_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "creeper_scarecrow"));
	public static final TagKey<Block> SKELETON_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "skeleton_scarecrow"));
	public static final TagKey<Block> SPIDER_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "spider_scarecrow"));
	public static final TagKey<Block> ZOMBIE_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "zombie_scarecrow"));
	public static final TagKey<Block> TURTLE_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "turtle_scarecrow"));
	public static final TagKey<Block> ENDERMITE_SCARECROW = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "endermite_scarecrow"));
	public static final TagKey<Block> PLUSHIE = TagKey.of(Registry.BLOCK_KEY, new Identifier("mobscarecrow", "plushie"));
}
