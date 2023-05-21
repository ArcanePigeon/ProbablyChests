package org.cloudwarp.probablychests.screenhandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;

public class PCChestScreenHandler extends ScreenHandler {
	private static final int columns = 9;
	private final Inventory inventory;
	private final int rows = 6;
	public PCChestScreenHandler (int syncId, PlayerInventory playerInventory){
		this(PCScreenHandlerType.PC_CHEST,syncId,playerInventory,new SimpleInventory(54));
	}


	public static PCChestScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		return new PCChestScreenHandler(PCScreenHandlerType.PC_CHEST, syncId, playerInventory, inventory);
	}

	public PCChestScreenHandler (ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(type, syncId);
		int k;
		int j;
		checkSize(inventory, rows * columns);
		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);
		int i = (this.rows - 4) * 18;

		for (j = 0; j < this.rows; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}
		for (j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
		}

	}

	@Override
	public ItemStack quickMove (PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < this.rows * 9) {
				if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}

		return itemStack;
	}

	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public void onClosed (PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}

	/*public void close(PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}*/

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getRows() {
		return this.rows;
	}

}