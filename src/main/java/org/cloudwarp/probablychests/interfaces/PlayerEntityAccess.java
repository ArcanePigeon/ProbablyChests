package org.cloudwarp.probablychests.interfaces;

import net.minecraft.inventory.Inventory;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

import java.util.UUID;

public interface PlayerEntityAccess {

	void openMimicInventory (PCTameablePetWithInventory horse, Inventory inventory);

	void addPetMimicToOwnedList (UUID mimic);

	void removePetMimicFromOwnedList (UUID mimic);

	int abandonMimics ();

	int getNumberOfPetMimics ();

	boolean checkForMimicLimit();

	void addMimicToKeepList(UUID mimic);
	void removeMimicFromKeepList(UUID mimic);
	boolean isMimicInKeepList(UUID mimic);
}
