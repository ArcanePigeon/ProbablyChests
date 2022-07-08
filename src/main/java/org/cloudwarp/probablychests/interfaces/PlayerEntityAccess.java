package org.cloudwarp.probablychests.interfaces;

import net.minecraft.inventory.Inventory;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

public interface PlayerEntityAccess {

	 void openMimicInventory(PCTameablePetWithInventory horse, Inventory inventory);
}
