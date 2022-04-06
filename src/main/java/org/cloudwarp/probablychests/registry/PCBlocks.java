package org.cloudwarp.probablychests.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class PCBlocks {
	private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
	public static final Block LUSH_CHEST = create("lush_chest", new PCChestBlock(PCChestTypes.LUSH.setting(), PCChestTypes.LUSH), true);
	public static final Block NORMAL_CHEST = create("normal_chest", new PCChestBlock(PCChestTypes.NORMAL.setting(), PCChestTypes.LUSH), true);
	public static final Block ROCKY_CHEST = create("rocky_chest", new PCChestBlock(PCChestTypes.ROCKY.setting(), PCChestTypes.LUSH), true);
	public static final Block POT = create("normal_pot", new NormalPot(), true);

	private static <T extends Block> T create (String name, T block, boolean createItem) {
		BLOCKS.put(block, new Identifier(ProbablyChests.MOD_ID, name));
		if (createItem) {
			ITEMS.put(new BlockItem(block, new Item.Settings().group(ProbablyChests.PROBABLY_CHESTS_GROUP)), BLOCKS.get(block));
		}
		return block;
	}

	public static void init () {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

}
