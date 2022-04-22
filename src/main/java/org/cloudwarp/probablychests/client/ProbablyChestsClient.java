package org.cloudwarp.probablychests.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.registry.PCBlockEntities;
import org.cloudwarp.probablychests.registry.PCEntities;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;
import software.bernie.example.GeckoLibMod;

@Environment(EnvType.CLIENT)
public class ProbablyChestsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient () {
		GeckoLibMod.DISABLE_IN_DEV = true;
		// The IDE is lying to you the type casting is necessary
		ScreenRegistry.<PCScreenHandler, CottonInventoryScreen<PCScreenHandler>>register(PCScreenHandlerType.PC_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
		ScreenRegistry.<PCMimicScreenHandler, CottonInventoryScreen<PCMimicScreenHandler>>register(PCScreenHandlerType.PC_CHEST_MIMIC, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));

		BlockEntityRendererRegistry.register(PCBlockEntities.LUSH_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.LUSH.name));
		BlockEntityRendererRegistry.register(PCBlockEntities.NORMAL_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.NORMAL.name));
		BlockEntityRendererRegistry.register(PCBlockEntities.ROCKY_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.ROCKY.name));
		BlockEntityRendererRegistry.register(PCBlockEntities.STONE_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.STONE.name));
		BlockEntityRendererRegistry.register(PCBlockEntities.GOLD_CHEST_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new PCChestRenderer(PCChestTypes.GOLD.name));
		//---------------------------------------------
		EntityRendererRegistry.register(PCEntities.NORMAL_CHEST_MIMIC, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "normal_mimic"));
		EntityRendererRegistry.register(PCEntities.LUSH_CHEST_MIMIC, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "lush_mimic"));
		EntityRendererRegistry.register(PCEntities.ROCKY_CHEST_MIMIC, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "rocky_mimic"));
		EntityRendererRegistry.register(PCEntities.STONE_CHEST_MIMIC, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "stone_mimic"));
		EntityRendererRegistry.register(PCEntities.GOLD_CHEST_MIMIC, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicRenderer(rendererDispatcherIn, "gold_mimic"));
		//---------------------------------------------
		EntityRendererRegistry.register(PCEntities.NORMAL_CHEST_MIMIC_PET, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "normal_mimic"));
		EntityRendererRegistry.register(PCEntities.LUSH_CHEST_MIMIC_PET, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "lush_mimic"));
		EntityRendererRegistry.register(PCEntities.ROCKY_CHEST_MIMIC_PET, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "rocky_mimic"));
		EntityRendererRegistry.register(PCEntities.STONE_CHEST_MIMIC_PET, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "stone_mimic"));
		EntityRendererRegistry.register(PCEntities.GOLD_CHEST_MIMIC_PET, (EntityRendererFactory.Context rendererDispatcherIn) -> new PCChestMimicPetRenderer(rendererDispatcherIn, "gold_mimic"));
	}
}
