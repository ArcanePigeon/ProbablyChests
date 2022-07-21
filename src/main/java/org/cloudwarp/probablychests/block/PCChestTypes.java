package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.SeaPickleBlock;
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
	GOLD(54, 9, new Identifier(ProbablyChests.MOD_ID, "gold_chest"), "gold_chest"),
	NETHER(54, 9, new Identifier(ProbablyChests.MOD_ID, "nether_chest"), "nether_chest"),
	SHADOW(54, 9, new Identifier(ProbablyChests.MOD_ID, "shadow_chest"), "shadow_chest"),
	ICE(54, 9, new Identifier(ProbablyChests.MOD_ID, "ice_chest"), "ice_chest"),
	CORAL(54, 9, new Identifier(ProbablyChests.MOD_ID, "coral_chest"), "coral_chest");


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
			case LUSH   -> PCEntities.LUSH_CHEST_MIMIC;
			case ROCKY  -> PCEntities.ROCKY_CHEST_MIMIC;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC;
			case STONE  -> PCEntities.STONE_CHEST_MIMIC;
			case GOLD   -> PCEntities.GOLD_CHEST_MIMIC;
			case NETHER -> PCEntities.NETHER_CHEST_MIMIC;
			case SHADOW -> PCEntities.SHADOW_CHEST_MIMIC;
			case ICE    -> PCEntities.ICE_CHEST_MIMIC;
			case CORAL  -> PCEntities.CORAL_CHEST_MIMIC;
			default     -> PCEntities.NORMAL_CHEST_MIMIC;
		};
	}

	public Identifier getLootTable () {
		return switch (this) {
			case LUSH   -> PCLootTables.LUSH_CHEST;
			case ROCKY  -> PCLootTables.ROCKY_CHEST;
			case NORMAL -> PCLootTables.NORMAL_CHEST;
			case STONE  -> PCLootTables.STONE_CHEST;
			case GOLD   -> PCLootTables.GOLD_CHEST;
			case NETHER -> PCLootTables.NETHER_CHEST;
			case SHADOW -> PCLootTables.SHADOW_CHEST;
			case ICE    -> PCLootTables.ICE_CHEST;
			case CORAL  -> PCLootTables.CORAL_CHEST;
			default     -> PCLootTables.NORMAL_CHEST;
		};
	}


	public int getRowCount () {
		return this.size / this.rowLength;
	}

	public BlockEntityType<? extends PCChestBlockEntity> getBlockEntityType () {
		return switch (this) {
			case LUSH   -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY;
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
			case ROCKY  -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY;
			case STONE  -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY;
			case GOLD   -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY;
			case NETHER -> PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY;
			case SHADOW -> PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY;
			case ICE    -> PCBlockEntities.ICE_CHEST_BLOCK_ENTITY;
			case CORAL  -> PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY;
			default     -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY;
		};
	}

	public PCChestBlockEntity makeEntity (BlockPos pos, BlockState state) {
		return switch (this) {
			case LUSH   -> PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case NORMAL -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case ROCKY  -> PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case STONE  -> PCBlockEntities.STONE_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case GOLD   -> PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case NETHER -> PCBlockEntities.NETHER_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case SHADOW -> PCBlockEntities.SHADOW_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case ICE    -> PCBlockEntities.ICE_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			case CORAL  -> PCBlockEntities.CORAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
			default     -> PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY.instantiate(pos, state);
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
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.WOOD);
			case ROCKY, STONE -> FabricBlockSettings.of(Material.STONE)
					.hardness(2.0F)
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.STONE);
			case GOLD, SHADOW -> FabricBlockSettings.of(Material.METAL)
					.hardness(2.0F)
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.METAL);
			case ICE -> FabricBlockSettings.of(Material.DENSE_ICE)
					.hardness(2.0F)
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.GLASS)
					.luminance(state -> 7)
					.slipperiness(0.98f);
			case CORAL -> FabricBlockSettings.of(Material.WOOD)
					.hardness(2.0F)
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.CORAL)
					.luminance(state -> PCChestBlock.isDry(state) ? 0 : 12)
					.slipperiness(0.99f);
			case NETHER -> FabricBlockSettings.of(Material.STONE)
					.hardness(2.0F)
					.resistance(3600000.0f)
					.sounds(BlockSoundGroup.STONE)
					.luminance(state -> 9);
			default -> FabricBlockSettings.of(Material.WOOD);
		};
	}

	public EntityType<? extends PCTameablePetWithInventory> getPetMimicType () {
		return switch (this) {
			case LUSH   -> PCEntities.LUSH_CHEST_MIMIC_PET;
			case ROCKY  -> PCEntities.ROCKY_CHEST_MIMIC_PET;
			case NORMAL -> PCEntities.NORMAL_CHEST_MIMIC_PET;
			case STONE  -> PCEntities.STONE_CHEST_MIMIC_PET;
			case GOLD   -> PCEntities.GOLD_CHEST_MIMIC_PET;
			case NETHER -> PCEntities.NETHER_CHEST_MIMIC_PET;
			case SHADOW -> PCEntities.SHADOW_CHEST_MIMIC_PET;
			case ICE    -> PCEntities.ICE_CHEST_MIMIC_PET;
			case CORAL  -> PCEntities.CORAL_CHEST_MIMIC_PET;
			default     -> PCEntities.NORMAL_CHEST_MIMIC_PET;
		};
	}
}