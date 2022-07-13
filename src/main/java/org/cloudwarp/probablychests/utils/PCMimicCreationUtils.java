package org.cloudwarp.probablychests.utils;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCStatistics;

import static org.cloudwarp.probablychests.block.PCChestBlock.FACING;
import static org.cloudwarp.probablychests.block.PCChestBlock.WATERLOGGED;

public class PCMimicCreationUtils {

	public static boolean createMimic (World world, BlockPos pos, BlockState state, PCChestTypes type, PlayerEntity player) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			boolean wasSecretMimic = checkForSecretMimic(chest, world, pos, type);
			if (!wasSecretMimic){
				return false;
			}
			if (world.getDifficulty() != Difficulty.PEACEFUL && chest.isMimic) {
				createMimic(false,pos,state,world,chest, player, type);
				return true;
			}
		}
		return false;
	}
	public static void createMimic(boolean isPetMimic, BlockPos pos, BlockState state, World world, PCChestBlockEntity chest, PlayerEntity player, PCChestTypes type){
		if(chest.hasMadeMimic){
			return;
		}
		chest.hasMadeMimic = true;
		PCTameablePetWithInventory mimic;
		if(isPetMimic){
			mimic = new PCChestMimicPet(type.getPetMimicType(), world);
			mimic.setOwner(player);
			mimic.setTarget((LivingEntity) null);
			mimic.setSitting(true);
			((PlayerEntityAccess)player).addPetMimicToOwnedList(mimic.getUuid());
			if(player != null) {
				Criteria.SUMMONED_ENTITY.trigger((ServerPlayerEntity) player, mimic);
			}
		}else{
			mimic = new PCChestMimic(type.getMimicType(), world);
			if(player != null) {
				player.increaseStat(PCStatistics.MIMIC_ENCOUNTERS, 1);
				Criteria.SUMMONED_ENTITY.trigger((ServerPlayerEntity) player, mimic);
			}
		}
		mimic.setType(type);
		mimic.setPos(pos.getX() + 0.5D, pos.getY()+0.1D, pos.getZ() + 0.5D);
		mimic.setYaw(state.get(FACING).asRotation());
		mimic.headYaw = mimic.getYaw();
		mimic.bodyYaw = mimic.getYaw();
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setStack(i, chest.getStack(i));
			chest.setStack(i, ItemStack.EMPTY);
		}
		world.spawnEntity(mimic);
		if(isPetMimic){
			mimic.world.sendEntityStatus(mimic, (byte) 7);
		}
		boolean waterlogged = state.get(WATERLOGGED);
		world.setBlockState(pos, waterlogged ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
	}
	public static void convertPetMimicToHostile (World world, PCChestTypes type, PCTameablePetWithInventory other){
		PCTameablePetWithInventory mimic;
		mimic = new PCChestMimic(type.getMimicType(), world);
		mimic.setType(type);
		mimic.copyPositionAndRotation(other);
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setStack(i, other.inventory.getStack(i));
			other.inventory.setStack(i, ItemStack.EMPTY);
		}
		if (other.hasCustomName()) {
			mimic.setCustomName(other.getCustomName());
			mimic.setCustomNameVisible(other.isCustomNameVisible());
		}
		other.discard();
		world.spawnEntity(mimic);
	}
	public static void convertHostileMimicToPet (World world, PCChestTypes type, PCTameablePetWithInventory other, PlayerEntity player){
		PCTameablePetWithInventory mimic;
		mimic = new PCChestMimicPet(type.getPetMimicType(), world);
		mimic.setOwner(player);
		mimic.setTarget((LivingEntity) null);
		mimic.setSitting(true);
		((PlayerEntityAccess)player).addPetMimicToOwnedList(mimic.getUuid());
		mimic.setType(type);
		mimic.copyPositionAndRotation(other);
		for (int i = 0; i < type.size; i++) {
			mimic.inventory.setStack(i, other.inventory.getStack(i));
			other.inventory.setStack(i, ItemStack.EMPTY);
		}
		if (other.hasCustomName()) {
			mimic.setCustomName(other.getCustomName());
			mimic.setCustomNameVisible(other.isCustomNameVisible());
		}
		other.discard();
		world.spawnEntity(mimic);
		mimic.world.sendEntityStatus(mimic, (byte) 7);
	}
	public static boolean checkForSecretMimic (PCChestBlockEntity chest, World world, BlockPos pos, PCChestTypes type) {
		PCConfig config = ProbablyChests.loadedConfig;
		if(chest.isMimic){
			return true;
		}
		if (! chest.hasBeenOpened && chest.isNatural) {
			chest.hasBeenOpened = true;
			float mimicRandom = world.getRandom().nextFloat();
			chest.isMimic = mimicRandom < config.worldGen.secretMimicChance;
			if (chest.isMimic) {
				return true;
			}
			LootableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, type.getLootTable());
		}
		return false;
	}

	public static boolean createPetMimic (World world, BlockPos pos, BlockState state, PlayerEntity player, PCChestTypes type) {
		PCChestBlockEntity chest = getChestBlockFromWorld(world, pos);
		if (chest != null) {
			boolean wasSecretMimic = checkForSecretMimic(chest, world, pos, type);
			if(wasSecretMimic){
				return false;
			}
			if(((PlayerEntityAccess)player).checkForMimicLimit()){
				return false;
			}
			createMimic(true,pos,state,world,chest, player, type);
			return true;
		}
		return false;
	}

	public static PCChestBlockEntity getChestBlockFromWorld (World world, BlockPos pos) {
		PCChestBlockEntity chest = null;
		if (world.getBlockEntity(pos) instanceof PCChestBlockEntity) {
			chest = (PCChestBlockEntity) world.getBlockEntity(pos);
		}
		return chest;
	}
}
