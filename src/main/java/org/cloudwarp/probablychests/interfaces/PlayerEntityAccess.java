package org.cloudwarp.probablychests.interfaces;

import net.minecraft.inventory.Inventory;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

import java.util.HashSet;
import java.util.UUID;

public interface PlayerEntityAccess {

	void openMimicInventory (PCTameablePetWithInventory horse, Inventory inventory);

	void addPetMimic (UUID mimic);

	void removePetMimic (UUID mimic);

	void removeAllPetMimics ();

	int getNumberOfPetMimics ();

	boolean checkForMimicLimit();
}
