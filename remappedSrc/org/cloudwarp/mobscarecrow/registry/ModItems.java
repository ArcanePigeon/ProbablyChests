package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.mobscarecrow.blockdetails.MobScarecrowItem;

import java.util.HashMap;

public class ModItems {
	public static final ItemGroup MOB_SCARECROW_GROUP = FabricItemGroupBuilder.create(
					new Identifier("mobscarecrow", "general"))
			.icon(() -> new ItemStack(ModBlocks.MOB_SCARECROW))
			.build();
	private static final HashMap<String, Item> ITEMS = new HashMap<>();

	private static void registerItem (String id, Item item) {
		ITEMS.put(id, Registry.register(Registry.ITEM, "mobscarecrow:" + id, item));
	}

	public static void registerItems () {
		if (! ITEMS.isEmpty()) {
			return;
		}
		registerItem("scarecrow", new MobScarecrowItem(ModBlocks.MOB_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("creeper_scarecrow", new MobScarecrowItem(ModBlocks.CREEPER_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("skeleton_scarecrow", new MobScarecrowItem(ModBlocks.SKELETON_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("spider_scarecrow", new MobScarecrowItem(ModBlocks.SPIDER_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("zombie_scarecrow", new MobScarecrowItem(ModBlocks.ZOMBIE_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("turtle_scarecrow", new MobScarecrowItem(ModBlocks.TURTLE_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("endermite_scarecrow", new MobScarecrowItem(ModBlocks.ENDERMITE_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("creeper_scarecrow_statue", new MobScarecrowItem(ModBlocks.CREEPER_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("skeleton_scarecrow_statue", new MobScarecrowItem(ModBlocks.SKELETON_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("spider_scarecrow_statue", new MobScarecrowItem(ModBlocks.SPIDER_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("zombie_scarecrow_statue", new MobScarecrowItem(ModBlocks.ZOMBIE_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("turtle_scarecrow_statue", new MobScarecrowItem(ModBlocks.TURTLE_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("endermite_scarecrow_statue", new MobScarecrowItem(ModBlocks.ENDERMITE_SCARECROW_STATUE, new Item.Settings().group(MOB_SCARECROW_GROUP)));
		registerItem("skeleton_scarecrow_big", new MobScarecrowItem(ModBlocks.SKELETON_SCARECROW_BIG, new Item.Settings().group(MOB_SCARECROW_GROUP)));
	}

	public static Item get (String id) {
		return ITEMS.getOrDefault(id, Items.AIR);
	}
}
