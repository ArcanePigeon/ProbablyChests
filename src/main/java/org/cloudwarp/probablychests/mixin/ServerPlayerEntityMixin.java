package org.cloudwarp.probablychests.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements PlayerEntityAccess {

	protected ServerPlayerEntityMixin (EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

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

	HashSet<UUID> petMimicList = new HashSet<>();
	@Override
	public void addPetMimicToOwnedList (UUID mimic){
		petMimicList.add(mimic);
	}
	public boolean checkForMimicLimit(){
		if(ProbablyChests.loadedConfig.mimicSettings.doPetMimicLimit && getNumberOfPetMimics() >= ProbablyChests.loadedConfig.mimicSettings.petMimicLimit){
			return true;
		}
		return false;
	}
	@Override
	public void removePetMimicFromOwnedList (UUID mimic){
		petMimicList.remove(mimic);
	}

	@Override
	public int getNumberOfPetMimics(){
		return petMimicList.size();
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
	public void writeCustomDataToNbt (NbtCompound nbt, CallbackInfo ci) {
		NbtList listnbt = new NbtList();
		for(Iterator<UUID> i = petMimicList.iterator(); i.hasNext();){
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
			addPetMimicToOwnedList(compoundnbt.getUuid("uuid"));
		}
	}
	HashSet<UUID> mimicKeepList = new HashSet<>();

	public void addMimicToKeepList(UUID mimic){
		mimicKeepList.add(mimic);
	}
	public void removeMimicFromKeepList(UUID mimic){
		mimicKeepList.remove(mimic);
	}
	public boolean isMimicInKeepList(UUID mimic){
		return mimicKeepList.contains(mimic);
	}
	@Override
	public int abandonMimics (){
		int removed = 0;
		for(Iterator<UUID> i = petMimicList.iterator(); i.hasNext();) {
			UUID mimic = i.next();
			if(!isMimicInKeepList(mimic)){
				PCTameablePetWithInventory entity = (PCTameablePetWithInventory)((ServerWorld)this.world).getEntity(mimic);
				if(entity != null && !entity.isRemoved()){
					entity.setIsAbandoned(true);
				}
				i.remove();
				removed++;
			}
		}
		return removed;
	}
}
