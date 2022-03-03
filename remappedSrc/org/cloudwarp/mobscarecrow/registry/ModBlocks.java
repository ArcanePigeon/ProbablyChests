package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.mobscarecrow.blockentities.*;
import org.cloudwarp.mobscarecrow.blocks.*;

public class ModBlocks {
	// BLOCKS
	public static final Block MOB_SCARECROW = new MobScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.WOOD));
	public static final Block CREEPER_SCARECROW = new CreeperScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block SKELETON_SCARECROW = new SkeletonScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block SPIDER_SCARECROW = new SpiderScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block ZOMBIE_SCARECROW = new ZombieScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block TURTLE_SCARECROW = new TurtleScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block ENDERMITE_SCARECROW = new EndermiteScarecrowBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
	public static final Block CREEPER_SCARECROW_STATUE = new CreeperScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block SKELETON_SCARECROW_STATUE = new SkeletonScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block SPIDER_SCARECROW_STATUE = new SpiderScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block ZOMBIE_SCARECROW_STATUE = new ZombieScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block TURTLE_SCARECROW_STATUE = new TurtleScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block ENDERMITE_SCARECROW_STATUE = new EndermiteScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE));
	public static final Block SKELETON_SCARECROW_BIG = new SkeletonScarecrowBigBlock(FabricBlockSettings.of(Material.WOOL).breakByHand(true).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));

	public static void registerBlocks () {
		Registry.register(Registry.BLOCK, "mobscarecrow:scarecrow", MOB_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:creeper_scarecrow", CREEPER_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:skeleton_scarecrow", SKELETON_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:spider_scarecrow", SPIDER_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:zombie_scarecrow", ZOMBIE_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:turtle_scarecrow", TURTLE_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:endermite_scarecrow", ENDERMITE_SCARECROW);
		Registry.register(Registry.BLOCK, "mobscarecrow:creeper_scarecrow_statue", CREEPER_SCARECROW_STATUE);
		Registry.register(Registry.BLOCK, "mobscarecrow:skeleton_scarecrow_statue", SKELETON_SCARECROW_STATUE);
		Registry.register(Registry.BLOCK, "mobscarecrow:spider_scarecrow_statue", SPIDER_SCARECROW_STATUE);
		Registry.register(Registry.BLOCK, "mobscarecrow:zombie_scarecrow_statue", ZOMBIE_SCARECROW_STATUE);
		Registry.register(Registry.BLOCK, "mobscarecrow:turtle_scarecrow_statue", TURTLE_SCARECROW_STATUE);
		Registry.register(Registry.BLOCK, "mobscarecrow:endermite_scarecrow_statue", ENDERMITE_SCARECROW_STATUE);
		//Registry.register(Registry.BLOCK, "mobscarecrow:skeleton_scarecrow_big", SKELETON_SCARECROW_BIG);
	}

	public static void RegisterBlockEntities () {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:scarecrow_block_entity", SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:creeper_scarecrow_block_entity", CREEPER_SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:skeleton_scarecrow_block_entity", SKELETON_SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:spider_scarecrow_block_entity", SPIDER_SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:zombie_scarecrow_block_entity", ZOMBIE_SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:turtle_scarecrow_block_entity", TURTLE_SCARECROW_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:endermite_scarecrow_block_entity", ENDERMITE_SCARECROW_BLOCK_ENTITY);
		//Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:skeleton_scarecrow_big_block_entity", SKELETON_SCARECROW_BIG_BLOCK_ENTITY);
	}

	// BLOCK ENTITIES
	public static final BlockEntityType<MobScarecrowBlockEntity> SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			MobScarecrowBlockEntity::new,
			MOB_SCARECROW
	).build(null);

	public static final BlockEntityType<CreeperScarecrowBlockEntity> CREEPER_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			CreeperScarecrowBlockEntity::new,
			CREEPER_SCARECROW,
			CREEPER_SCARECROW_STATUE
	).build(null);

	public static final BlockEntityType<SkeletonScarecrowBlockEntity> SKELETON_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			SkeletonScarecrowBlockEntity::new,
			SKELETON_SCARECROW,
			SKELETON_SCARECROW_STATUE
	).build(null);

	public static final BlockEntityType<SpiderScarecrowBlockEntity> SPIDER_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			SpiderScarecrowBlockEntity::new,
			SPIDER_SCARECROW,
			SPIDER_SCARECROW_STATUE
	).build(null);

	public static final BlockEntityType<ZombieScarecrowBlockEntity> ZOMBIE_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			ZombieScarecrowBlockEntity::new,
			ZOMBIE_SCARECROW,
			ZOMBIE_SCARECROW_STATUE
	).build(null);

	public static final BlockEntityType<TurtleScarecrowBlockEntity> TURTLE_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			TurtleScarecrowBlockEntity::new,
			TURTLE_SCARECROW
	).build(null);

	public static final BlockEntityType<EndermiteScarecrowBlockEntity> ENDERMITE_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			EndermiteScarecrowBlockEntity::new,
			ENDERMITE_SCARECROW
	).build(null);

	public static final BlockEntityType<SkeletonScarecrowBigBlockEntity> SKELETON_SCARECROW_BIG_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			SkeletonScarecrowBigBlockEntity::new,
			SKELETON_SCARECROW_BIG
	).build(null);

}
