package org.cloudwarp.probablychests.blockdetails;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class PCBlockTags {
	public static final TagKey<Block> MIMIC_CHEST = TagKey.of(Registry.BLOCK_KEY, new Identifier("probablychests", "mimic"));
}
