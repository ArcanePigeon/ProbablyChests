package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.mobscarecrow.MobScarecrow;
import org.cloudwarp.mobscarecrow.MobScarecrowItem;

import java.util.HashMap;

public class ModItems {
    public static final ItemGroup MOB_SCARECROW_GROUP = FabricItemGroupBuilder.create(
                    new Identifier("mobscarecrow", "general"))
            .icon(() -> new ItemStack(ModBlocks.MOB_SCARECROW))
            .build();
    private static final HashMap<String, Item> ITEMS = new HashMap<>();
    private static void registerItem(String id, Item item){
        ITEMS.put(id, Registry.register(Registry.ITEM, "mobscarecrow:"+id, item));
    }
    public static void registerItems() {
        if(!ITEMS.isEmpty()){
            return;
        }
        registerItem("scarecrow",new MobScarecrowItem(ModBlocks.MOB_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
        registerItem("creeperscarecrow",new MobScarecrowItem(ModBlocks.CREEPER_SCARECROW, new Item.Settings().group(MOB_SCARECROW_GROUP)));
    }
    public static Item get(String id) {
        return ITEMS.getOrDefault(id, Items.AIR);
    }
}
