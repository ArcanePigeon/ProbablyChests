package org.cloudwarp.probablychests.registry;

import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class PCBiomeTags {
	public static TagKey<Biome> IS_LUSH;
	public static TagKey<Biome> IS_DRIPSTONE;
	public static void init(){
		IS_LUSH = of("lush_caves");
		IS_DRIPSTONE = of("dripstone_caves");
	}
	private static TagKey<Biome> of(String id) {
		return TagKey.of(Registry.BIOME_KEY, new Identifier(id));
	}
}
