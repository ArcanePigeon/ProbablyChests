package org.cloudwarp.probablychests.screenhandlers;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;

import java.util.function.Supplier;

public class PCMimicScreenHandler extends ScreenHandler {
	private static final int columns = 9;
	private final Inventory inventory;
	private final int rows;

	public PCMimicScreenHandler (int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(54), 6);
	}

	public PCMimicScreenHandler (int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
		super(PCScreenHandlerType.PC_CHEST_MIMIC, syncId);
		checkSize(inventory, 54);
		this.inventory = inventory;
		this.rows = rows;
		inventory.onOpen(playerInventory.player);
		createXRows(syncId, playerInventory, inventory, rows);
	}


	public static PCMimicScreenHandler createXRows (int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
		return new PCMimicScreenHandler(PCScreenHandlerType.PC_CHEST_MIMIC, syncId, playerInventory, inventory, rows);
	}

	public PCMimicScreenHandler (ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
		super(type, syncId);
		checkSize(inventory, rows * columns);
		this.inventory = inventory;
		this.rows = rows;
		inventory.onOpen(playerInventory.player);
		int i = (this.rows - 4) * 18;

		int j;
		int k;
		for (j = 0; j < this.rows; ++ j) {
			for (k = 0; k < 9; ++ k) {
				this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (j = 0; j < 3; ++ j) {
			for (k = 0; k < 9; ++ k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++ j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
		}

	}

	public boolean canUse (PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	public ItemStack transferSlot (PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < this.rows * 9) {
				if (! this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (! this.insertItem(itemStack2, 0, this.rows * 9, false)) {
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

	public void close (PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}

	public Inventory getInventory () {
		return this.inventory;
	}

	public int getRows () {
		return this.rows;
	}
}