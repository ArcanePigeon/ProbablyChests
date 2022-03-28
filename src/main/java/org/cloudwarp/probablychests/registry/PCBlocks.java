package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copyOf;
import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.of;

public class PCBlocks {

	public static final Block LUSH_CHEST = new LushChestBlock();


	public static void init() {
		Registry.register(Registry.BLOCK, new Identifier(ProbablyChests.MOD_ID,"lush_chest"), LUSH_CHEST);
	}


}
