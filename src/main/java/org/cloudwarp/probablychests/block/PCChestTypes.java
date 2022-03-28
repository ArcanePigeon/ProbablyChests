package org.cloudwarp.probablychests.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.registry.PCBlockEntityTypes;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public enum PCChestTypes {
	LUSH(54,9, new Identifier(ProbablyChests.MOD_ID, "entity/chest/lush_chest"));

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

	public ChestBlockEntity getEntity(BlockPos pos, BlockState state) {
		return switch (this) {
			case LUSH -> PCBlockEntityTypes.PC_CHEST_BLOCK_ENTITY_TYPE.instantiate(pos, state);
			default -> new ChestBlockEntity(pos, state);
		};
	}

	public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
		return switch (this) {
			case LUSH -> PCBlockEntityTypes.PC_CHEST_BLOCK_ENTITY_TYPE;
			default -> BlockEntityType.CHEST;
		};
	}

	public ScreenHandlerType<PCScreenHandler> getScreenHandlerType() {
		return switch (this) {
			case LUSH -> PCScreenHandlerType.LUSH_CHEST;
			default -> PCScreenHandlerType.LUSH_CHEST;
		};
	}
}
