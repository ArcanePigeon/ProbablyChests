package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.mobscarecrow.blockentities.*;
import org.cloudwarp.mobscarecrow.blocks.*;

public class ModBlocks {
	// BLOCKS
	public static final Block MOB_SCARECROW = new MobScarecrowBlock(FabricBlockSettings.of(Material.NETHER_WOOD).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.WOOD).nonOpaque());
	public static final Block CREEPER_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.CREEPER_SCARECROW_VOXELSHAPE);
	public static final Block SKELETON_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.SKELETON_SCARECROW_VOXELSHAPE);
	public static final Block SPIDER_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.SPIDER_SCARECROW_VOXELSHAPE);
	public static final Block ZOMBIE_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.ZOMBIE_SCARECROW_VOXELSHAPE);
	public static final Block TURTLE_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.TURTLE_SCARECROW_VOXELSHAPE);
	public static final Block ENDERMITE_SCARECROW = new SmallScarecrowBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE).nonOpaque(),
			ModVoxelShapes.ENDERMITE_SCARECROW_VOXELSHAPE);
	public static final Block CREEPER_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.CREEPER_SCARECROW_VOXELSHAPE);
	public static final Block SKELETON_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.SKELETON_SCARECROW_VOXELSHAPE);
	public static final Block SPIDER_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.SPIDER_SCARECROW_VOXELSHAPE);
	public static final Block ZOMBIE_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.ZOMBIE_SCARECROW_VOXELSHAPE);
	public static final Block TURTLE_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.TURTLE_SCARECROW_VOXELSHAPE);
	public static final Block ENDERMITE_SCARECROW_STATUE = new SmallScarecrowBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque(),
			ModVoxelShapes.ENDERMITE_SCARECROW_VOXELSHAPE);
	//public static final Block SKELETON_SCARECROW_BIG = new SkeletonScarecrowBigBlock(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1f).sounds(ModSounds.PLUSHIE));
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
		Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:small_scarecrow_block_entity", SMALL_SCARECROW_BLOCK_ENTITY);
		//Registry.register(Registry.BLOCK_ENTITY_TYPE, "mobscarecrow:big_scarecrow_block_entity", BIG_SCARECROW_BLOCK_ENTITY);
	}

	// BLOCK ENTITIES
	public static final BlockEntityType<MobScarecrowBlockEntity> SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			MobScarecrowBlockEntity::new,
			MOB_SCARECROW
	).build(null);

	public static final BlockEntityType<SmallScarecrowBlockEntity> SMALL_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			SmallScarecrowBlockEntity::new,
			SKELETON_SCARECROW,
			SKELETON_SCARECROW_STATUE,
			CREEPER_SCARECROW,
			CREEPER_SCARECROW_STATUE,
			SPIDER_SCARECROW,
			SPIDER_SCARECROW_STATUE,
			ZOMBIE_SCARECROW,
			ZOMBIE_SCARECROW_STATUE,
			TURTLE_SCARECROW,
			ENDERMITE_SCARECROW
	).build(null);

	/*public static final BlockEntityType<BigScarecrowBlockEntity> BIG_SCARECROW_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
			BigScarecrowBlockEntity::new,
			SKELETON_SCARECROW_BIG
	).build(null);*/

}
