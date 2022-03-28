package org.cloudwarp.probablychests.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.entity.LushChestBlockEntity;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;

public class PCBlockEntityTypes {
	public static BlockEntityType<LushChestBlockEntity> PC_CHEST_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(LushChestBlockEntity::new, PCBlocks.LUSH_CHEST).build();

	public static void init() {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ProbablyChests.MOD_ID, "lush_chest"), PC_CHEST_BLOCK_ENTITY_TYPE);
	}
}
