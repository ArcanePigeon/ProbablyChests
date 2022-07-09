package org.cloudwarp.probablychests.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.DataResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements PlayerEntityAccess {

	@Shadow
	public void closeHandledScreen() {}
	@Shadow
	private void incrementScreenHandlerSyncId() {}
	@Shadow public ServerPlayNetworkHandler networkHandler;
	@Shadow private int screenHandlerSyncId;
	@Shadow
	private void onScreenHandlerOpened(ScreenHandler screenHandler) {}
	@Override
	public void openMimicInventory(PCTameablePetWithInventory mimic, Inventory inventory) {
		if (this.currentScreenHandler != this.playerScreenHandler) {
			this.closeHandledScreen();
		}
		this.incrementScreenHandlerSyncId();
		this.networkHandler.sendPacket(new OpenMimicScreenS2CPacket(this.screenHandlerSyncId, inventory.size(), mimic.getId()));
		this.currentScreenHandler = new PCMimicScreenHandler(this.screenHandlerSyncId, this.getInventory(), inventory, mimic);
		this.onScreenHandlerOpened(this.currentScreenHandler);
	}
	// Mimic tracker

	HashSet<UUID> pet_mimics = new HashSet<>();
	@Override
	public void addPetMimic(UUID mimic){
		pet_mimics.add(mimic);
	}
	public boolean checkForMimicLimit(){
		if(ProbablyChests.loadedConfig.mimicSettings.doPetMimicLimit && getNumberOfPetMimics() >= ProbablyChests.loadedConfig.mimicSettings.petMimicLimit){
			return true;
		}
		return false;
	}
	@Override
	public void removePetMimic(UUID mimic){
		pet_mimics.remove(mimic);
	}
	@Override
	public void removeAllPetMimics(){
		pet_mimics.clear();
	}
	@Override
	public int getNumberOfPetMimics(){
		return pet_mimics.size();
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
	public void writeCustomDataToNbt (NbtCompound nbt, CallbackInfo ci) {
		NbtList listnbt = new NbtList();
		for(Iterator<UUID> i = pet_mimics.iterator(); i.hasNext();){
			UUID mimic = i.next();
			NbtCompound compoundnbt = new NbtCompound();
			compoundnbt.putUuid("uuid",mimic);
			listnbt.add(compoundnbt);

		}
		nbt.put("pet_mimics", listnbt);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V")
	public void readCustomDataFromNbt (NbtCompound nbt, CallbackInfo ci) {
		NbtList listnbt = nbt.getList("pet_mimics", 10);
		for (int i = 0; i < listnbt.size(); ++ i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			addPetMimic(compoundnbt.getUuid("uuid"));
		}
	}
}