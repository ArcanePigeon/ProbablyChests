package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.item.*;

public class PCItems {
	public static final Item MIMIC_KEY = new MimicKeyItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item MIMIC_KEY_FRAGMENT = new Item(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP));
	public static final Item MIMIC_CORE = new MimicCoreItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(1));
	public static final Item PET_MIMIC_KEY = new PetMimicKeyItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item MIMIC_HAND_BELL = new MimicHandBellItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(1));
	public static final Item IRON_KEY = new IronKeyItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item IRON_LOCK = new IronLockItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));

	public static final Item GOLD_KEY = new GoldKeyItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item GOLD_LOCK = new GoldLockItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));

	public static final Item VOID_KEY = new VoidKeyItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));
	public static final Item VOID_LOCK = new VoidLockItem(new FabricItemSettings().group(ProbablyChests.PROBABLY_CHESTS_GROUP).maxCount(16));

	public static void init () {
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_key"), MIMIC_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_key_fragment"), MIMIC_KEY_FRAGMENT);
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_core"), MIMIC_CORE);
		Registry.register(Registry.ITEM, ProbablyChests.id("pet_mimic_key"), PET_MIMIC_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("mimic_hand_bell"), MIMIC_HAND_BELL);
		Registry.register(Registry.ITEM, ProbablyChests.id("iron_key"), IRON_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("iron_lock"), IRON_LOCK);
		Registry.register(Registry.ITEM, ProbablyChests.id("gold_key"), GOLD_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("gold_lock"), GOLD_LOCK);
		Registry.register(Registry.ITEM, ProbablyChests.id("void_key"), VOID_KEY);
		Registry.register(Registry.ITEM, ProbablyChests.id("void_lock"), VOID_LOCK);
	}
}
