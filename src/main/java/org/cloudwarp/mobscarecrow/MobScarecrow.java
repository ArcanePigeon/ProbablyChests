package org.cloudwarp.mobscarecrow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;
import org.cloudwarp.mobscarecrow.registry.ModItems;

import java.util.Optional;

public class MobScarecrow implements ModInitializer {
    // REMINDER: If changing scarecrow model dont forget to update the texture path to "0" : "mobscarecrow:block/scarecrow"
    // TODO: add rotation
    // TODO: add breaking above first block
    // TODO: ony place on top of block
    // TODO: find out why the item is dark FRONT sid lighting issue, change to non side lit?
    // TODO: add mob fear
    // TODO: add particles
    // TODO: add sounds
    // TODO: Craftable scary pumpkins
    // TODO: Different scarecrow for different mobs
    // TODO: Craftable mob heads?
    // TODO: Make video on how I made my mod.
    // TODO: all craftable mob skulls made from skeleton head
    // Piglins run away from warped fungus, find that code
    /**
     * add brain to all hostile entities that don't spawn with brains
     * private Optional<BlockPos> findNearestWarpedFungus(ServerWorld world, HoglinEntity hoglin) {
     * return BlockPos.findClosest(hoglin.getBlockPos(), 8, 4, (blockPos) -> {
     * return world.getBlockState(blockPos).isIn(BlockTags.HOGLIN_REPELLENTS);
     * });
     * }
     * make scarecrow block living entity
     */


    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlocks.RegisterBlockEntities();
    }



}
