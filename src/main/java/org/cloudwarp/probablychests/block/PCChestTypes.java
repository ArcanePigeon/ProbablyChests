package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCBlocks;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public enum PCChestTypes {

	LUSH(54, 9, new Identifier(ProbablyChests.MOD_ID, "lush_chest"), "lush_chest"),
	NORMAL(54, 9, new Identifier(ProbablyChests.MOD_ID, "normal_chest"), "normal_chest"),
	ROCKY(54, 9, new Identifier(ProbablyChests.MOD_ID, "rocky_chest"), "rocky_chest");


	public final int size;
	public final int rowLength;
	public final Identifier texture;
	public final String name;

	PCChestTypes (int size, int rowLength, Identifier texture, String name) {
		this.size = size;
		this.rowLength = rowLength;
		this.texture = texture;
		this.name = name;
	}


	public int getRowCount () {
		return this.size / this.rowLength;
	}

	public BlockEntityType<? extends PCChestBlockEntity> getBlockEntityType () {
		return switch (this) {
			case LUSH -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY;
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
			case ROCKY -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY;
			default -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
		};
	}

	public PCChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
		return switch (this) {
			case LUSH -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case ROCKY -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			default -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
		};
	}

	public ScreenHandlerType<PCScreenHandler> getScreenHandlerType () {
		return switch (this) {
			default -> PCScreenHandlerType.PC_CHEST;
		};
	}
	public FabricBlockSettings setting() {
		return switch (this) {
			case LUSH, NORMAL -> FabricBlockSettings.of(Material.WOOD)
					.hardness(2.0F)
					.resistance(2.0F)
					.sounds(BlockSoundGroup.WOOD);
			case ROCKY -> FabricBlockSettings.of(Material.STONE)
					.hardness(2.0F)
					.resistance(2.0F)
					.sounds(BlockSoundGroup.STONE);
			default -> FabricBlockSettings.of(Material.WOOD);
		};
	}
}