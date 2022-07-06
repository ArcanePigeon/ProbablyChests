package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCItems {
	public static final Item MIMIC_KEY = new Item(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item MIMIC_KEY_FRAGMENT = new Item(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP));
	public static final Item MIMIC_CORE = new Item(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(1));
	public static final Item PET_MIMIC_KEY = new Item(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));

	public static void init () {
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_key"), MIMIC_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_key_fragment"), MIMIC_KEY_FRAGMENT);
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_core"), MIMIC_CORE);
		Registry.register(Registry.ITEM, ProbablyChests.id("pet_mimic_key"), PET_MIMIC_KEY);
	}
}
