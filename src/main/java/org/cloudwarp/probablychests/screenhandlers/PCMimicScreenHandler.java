package org.cloudwarp.probablychests.screenhandlers;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;

import java.util.function.Supplier;

public class PCMimicScreenHandler  extends SyncedGuiDescription {

	Inventory inventory;
	//PCChestTypes

	public PCMimicScreenHandler (ScreenHandlerType<?> type, PCChestTypes chestType, int syncId, PlayerInventory playerInventory, SimpleInventory inventory) {
		super(type, syncId, playerInventory, inventory, null);
		int rows = chestType.getRowCount();
		int length = chestType.rowLength;

		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);

		WItemSlot itemSlot;
		int counter = 0;
		if (chestType.rowLength == 1) {
			itemSlot = WItemSlot.of(blockInventory, 0);
			itemSlot.setFilter(stack -> stack.getItem() == Items.DIRT);
			root.add(itemSlot, (18 * 4), 12);
		} else {
			for (int j = 0; j < rows; j++) {
				for (int i = 0; i < length; i++) {
					itemSlot = WItemSlot.of(blockInventory, counter);
					root.add(itemSlot, (18 * i), 12 + (18 * j));
					counter++;
				}
			}
		}
		root.setInsets(Insets.ROOT_PANEL);

		int height = 15;
		height += 18 * (chestType.size / length);
		int width = 0;

		if (chestType.rowLength > 9) {
			width = 9 * (chestType.rowLength - 9);
		}

		root.add(this.createPlayerInventoryPanel(), width, height);
		root.validate(this);
	}

	public Inventory getInventory () {
		return this.inventory;
	}

}