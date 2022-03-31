package org.cloudwarp.probablychests.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public enum PCChestTypes {

	LUSH(54, 9, new Identifier(ProbablyChests.MOD_ID, "lush_chest"));


	public final int size;
	public final int rowLength;
	public final Identifier texture;

	PCChestTypes(int size, int rowLength, Identifier texture) {
		this.size = size;
		this.rowLength = rowLength;
		this.texture = texture;
	}

	public int getRowCount() {
		return this.size / this.rowLength;
	}

	public static Block get(PCChestTypes type) {
		return switch (type) {
			case LUSH -> PCBlocks.LUSH_CHEST;
			default -> Blocks.CHEST;
		};
	}

	public BlockEntityType<? extends PCChestBlockEntity> getBlockEntityType() {
		return switch (this) {
			case LUSH -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY;
			default -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY;
		};
	}

	public ScreenHandlerType<PCScreenHandler> getScreenHandlerType() {
		return switch (this) {
			case LUSH -> PCScreenHandlerType.LUSH_CHEST;
			default -> PCScreenHandlerType.LUSH_CHEST;
		};
	}
}