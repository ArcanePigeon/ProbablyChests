package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.mobscarecrow.blockentities.*;
import org.cloudwarp.mobscarecrow.blocks.*;
import org.cloudwarp.mobscarecrow.models.MobScarecrowModelProvider;

public class ModBlocks {
    // BLOCKS
    public static final Block MOB_SCARECROW = new MobScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().breakByTool(FabricToolTags.AXES,1).strength(2.0f,3f));
    public static final Block CREEPER_SCARECROW = new CreeperScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().breakByTool(FabricToolTags.AXES,1).strength(2.0f,3f));
    public static final Block SKELETON_SCARECROW = new SkeletonScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().breakByTool(FabricToolTags.AXES,1).strength(2.0f,3f));
    public static final Block SPIDER_SCARECROW = new SpiderScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().breakByTool(FabricToolTags.AXES,1).strength(2.0f,3f));
    public static final Block ZOMBIE_SCARECROW = new ZombieScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().breakByTool(FabricToolTags.AXES,1).strength(2.0f,3f));
    // BLOCK ENTITIES
    public static final BlockEntityType<MobScarecrowBlockEntity> SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
            MobScarecrowBlockEntity::new,MOB_SCARECROW
    ).build(null);

    public static final BlockEntityType<CreeperScarecrowBlockEntity> CREEPER_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
            CreeperScarecrowBlockEntity::new,CREEPER_SCARECROW
    ).build(null);

    public static final BlockEntityType<SkeletonScarecrowBlockEntity> SKELETON_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
            SkeletonScarecrowBlockEntity::new,SKELETON_SCARECROW
    ).build(null);

    public static final BlockEntityType<SpiderScarecrowBlockEntity> SPIDER_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
            SpiderScarecrowBlockEntity::new,SPIDER_SCARECROW
    ).build(null);

    public static final BlockEntityType<ZombieScarecrowBlockEntity> ZOMBIE_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
            ZombieScarecrowBlockEntity::new,ZOMBIE_SCARECROW
    ).build(null);

    public static void registerBlocks(){
        Registry.register(Registry.BLOCK, MobScarecrowModelProvider.SCARECROW_MODEL, MOB_SCARECROW);
        Registry.register(Registry.BLOCK, MobScarecrowModelProvider.CREEPER_SCARECROW_MODEL, CREEPER_SCARECROW);
        Registry.register(Registry.BLOCK, MobScarecrowModelProvider.SKELETON_SCARECROW_MODEL, SKELETON_SCARECROW);
        Registry.register(Registry.BLOCK, MobScarecrowModelProvider.SPIDER_SCARECROW_MODEL, SPIDER_SCARECROW);
        Registry.register(Registry.BLOCK, MobScarecrowModelProvider.ZOMBIE_SCARECROW_MODEL, ZOMBIE_SCARECROW);
    }
    public static void RegisterBlockEntities(){
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:scarecrow_block_entity", SCARECROW_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:creeper_scarecrow_block_entity", CREEPER_SCARECROW_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:skeleton_scarecrow_block_entity", SKELETON_SCARECROW_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:spider_scarecrow_block_entity", SPIDER_SCARECROW_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:zombie_scarecrow_block_entity", ZOMBIE_SCARECROW_BLOCK_ENTITY);
    }
}
