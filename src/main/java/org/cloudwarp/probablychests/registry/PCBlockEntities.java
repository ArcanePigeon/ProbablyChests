package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.cloudwarp.probablychests.registry.PCBlocks.*;

public class PCBlockEntities {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static void init () {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}

	private static <T extends BlockEntity> BlockEntityType<T> register (String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new Identifier(ProbablyChests.MOD_ID, name));
		return type;
	}

	//--------------------------------------------------------------
	public static final BlockEntityType<LushChestBlockEntity> LUSH_CHEST_BLOCK_ENTITY = register("lush_chest_block_entity", FabricBlockEntityTypeBuilder.create(LushChestBlockEntity::new, LUSH_CHEST).build(null));
	public static final BlockEntityType<NormalChestBlockEntity> NORMAL_CHEST_BLOCK_ENTITY = register("normal_chest_block_entity", FabricBlockEntityTypeBuilder.create(NormalChestBlockEntity::new, NORMAL_CHEST).build(null));
	public static final BlockEntityType<RockyChestBlockEntity> ROCKY_CHEST_BLOCK_ENTITY = register("rocky_chest_block_entity", FabricBlockEntityTypeBuilder.create(RockyChestBlockEntity::new, ROCKY_CHEST).build(null));
	public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY = register("stone_chest_block_entity", FabricBlockEntityTypeBuilder.create(StoneChestBlockEntity::new, STONE_CHEST).build(null));
	public static final BlockEntityType<GoldChestBlockEntity> GOLD_CHEST_BLOCK_ENTITY = register("gold_chest_block_entity", FabricBlockEntityTypeBuilder.create(GoldChestBlockEntity::new, GOLD_CHEST).build(null));

}
