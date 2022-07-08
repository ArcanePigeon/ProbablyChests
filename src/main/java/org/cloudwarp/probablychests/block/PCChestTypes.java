package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCLootTables;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;

public enum PCChestTypes {

	LUSH(54, 9, new Identifier(ProbablyChests.MOD_ID, "lush_chest"), "lush_chest"),
	NORMAL(54, 9, new Identifier(ProbablyChests.MOD_ID, "normal_chest"), "normal_chest"),
	ROCKY(54, 9, new Identifier(ProbablyChests.MOD_ID, "rocky_chest"), "rocky_chest"),
	STONE(54, 9, new Identifier(ProbablyChests.MOD_ID, "stone_chest"), "stone_chest"),
	GOLD(54, 9, new Identifier(ProbablyChests.MOD_ID, "gold_chest"), "gold_chest");


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

	public EntityType<PCChestMimic> getMimicType () {
		return switch (this) {
			case LUSH -> PCEntities.LUSH_CHEST_MIMIC;
			case ROCKY -> PCEntities.ROCKY_CHEST_MIMIC;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC;
			case STONE -> PCEntities.STONE_CHEST_MIMIC;
			case GOLD -> PCEntities.GOLD_CHEST_MIMIC;
			default -> PCEntities.NORMAL_CHEST_MIMIC;
		};
	}

	public Identifier getLootTable () {
		return switch (this) {
			case LUSH -> PCLootTables.LUSH_CHEST;
			case ROCKY -> PCLootTables.ROCKY_CHEST;
			case NORMAL -> PCLootTables.NORMAL_CHEST;
			//case STONE -> PCLootTables.STONE_CHEST;
			//case GOLD -> PCLootTables.GOLD_CHEST;
			default -> PCLootTables.NORMAL_CHEST;
		};
	}


	public int getRowCount () {
		return this.size / this.rowLength;
	}

	public BlockEntityType<? extends PCChestBlockEntity> getBlockEntityType () {
		return switch (this) {
			case LUSH -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY;
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
			case ROCKY -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY;
			case STONE -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY;
			case GOLD -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY;
			default -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
		};
	}

	public PCChestBlockEntity makeEntity (BlockPos pos, BlockState state) {
		return switch (this) {
			case LUSH -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case ROCKY -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case STONE -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case GOLD -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			default -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
		};
	}

	public ScreenHandlerType<PCChestScreenHandler> getScreenHandlerType () {
		return switch (this) {
			default -> PCScreenHandlerType.PC_CHEST;
		};
	}

	public FabricBlockSettings setting () {
		return switch (this) {
			case LUSH, NORMAL -> FabricBlockSettings.of(Material.WOOD)
					.hardness(2.0F)
					.resistance(10.0F)
					.sounds(BlockSoundGroup.WOOD);
			case ROCKY, STONE -> FabricBlockSettings.of(Material.STONE)
					.hardness(2.0F)
					.resistance(10.0F)
					.sounds(BlockSoundGroup.STONE);
			case GOLD -> FabricBlockSettings.of(Material.METAL)
					.hardness(2.0F)
					.resistance(10.0F)
					.sounds(BlockSoundGroup.METAL);
			default -> FabricBlockSettings.of(Material.WOOD);
		};
	}

	public EntityType<? extends PCTameablePetWithInventory> getPetMimicType () {
		return switch (this) {
			case LUSH -> PCEntities.LUSH_CHEST_MIMIC_PET;
			case ROCKY -> PCEntities.ROCKY_CHEST_MIMIC_PET;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC_PET;
			case STONE -> PCEntities.STONE_CHEST_MIMIC_PET;
			case GOLD -> PCEntities.GOLD_CHEST_MIMIC_PET;
			default -> PCEntities.NORMAL_CHEST_MIMIC_PET;
		};
	}
}