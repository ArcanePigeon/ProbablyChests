package org.cloudwarp.probablychests.screenhandlers;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.utils.PCEventHandler;

import java.util.UUID;

public class PCMimicScreenHandler extends ScreenHandler {
	private static final int columns = 9;
	private final Inventory inventory;
	private final int rows = 6;
	private PCTameablePetWithInventory entity;
	public PCMimicScreenHandler (int syncId, PlayerInventory playerInventory){
		this(PCScreenHandlerType.PC_CHEST_MIMIC,syncId,playerInventory,new SimpleInventory(54));
	}
	public PCMimicScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		this(PCScreenHandlerType.PC_CHEST_MIMIC, syncId, playerInventory, inventory);
	}

	public static PCMimicScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		return new PCMimicScreenHandler(PCScreenHandlerType.PC_CHEST_MIMIC, syncId, playerInventory, inventory);
	}

	public PCMimicScreenHandler (ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
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

	public void setMimicEntity(PCTameablePetWithInventory entity){
		this.entity = entity;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		if(this.entity.getIsMimicLocked() && player != this.entity.getOwner()){
			this.entity.bite(player);
			return false;
		}
		return !this.entity.areInventoriesDifferent(this.inventory) && this.inventory.canPlayerUse(player) && this.entity.isAlive() && this.entity.distanceTo(player) < 8.0f;
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
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

	public void close(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			if (this.entity != null) {
				entity.closeGui(player);
			} else {
				System.out.println("entity is null");
			}
		}
		super.close(player);
		this.inventory.onClose(player);
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getRows() {
		return this.rows;
	}

}