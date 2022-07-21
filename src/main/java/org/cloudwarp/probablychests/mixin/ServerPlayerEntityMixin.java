package org.cloudwarp.probablychests.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityAccess {


	public ServerPlayerEntityMixin (World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	HashSet<UUID> petMimicList = new HashSet<>();

	@Override
	public void addPetMimicToOwnedList (UUID mimic) {
		petMimicList.add(mimic);
	}

	public boolean checkForMimicLimit () {
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			PCTameablePetWithInventory entity = (PCTameablePetWithInventory) ((ServerWorld) this.world).getEntity(mimic);
			if (entity == null || entity.isRemoved()) {
				i.remove();
			}
		}
		if (ProbablyChests.loadedConfig.mimicSettings.doPetMimicLimit && getNumberOfPetMimics() >= ProbablyChests.loadedConfig.mimicSettings.petMimicLimit) {
			return true;
		}
		return false;
	}

	@Override
	public void removePetMimicFromOwnedList (UUID mimic) {
		petMimicList.remove(mimic);
	}

	@Override
	public int getNumberOfPetMimics () {
		return petMimicList.size();
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
	public void writeCustomDataToNbt (NbtCompound nbt, CallbackInfo ci) {
		NbtList listnbt = new NbtList();
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			NbtCompound compoundnbt = new NbtCompound();
			compoundnbt.putUuid("uuid", mimic);
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

	public void addMimicToKeepList (UUID mimic) {
		mimicKeepList.add(mimic);
	}

	public void removeMimicFromKeepList (UUID mimic) {
		mimicKeepList.remove(mimic);
	}

	public boolean isMimicInKeepList (UUID mimic) {
		return mimicKeepList.contains(mimic);
	}

	@Override
	public int abandonMimics () {
		int removed = 0;
		for (Iterator<UUID> i = petMimicList.iterator(); i.hasNext(); ) {
			UUID mimic = i.next();
			if (! isMimicInKeepList(mimic)) {
				PCTameablePetWithInventory entity = (PCTameablePetWithInventory) ((ServerWorld) this.world).getEntity(mimic);
				if (entity != null && ! entity.isRemoved()) {
					entity.setIsAbandoned(true);
				}
				i.remove();
				removed++;
			}
		}
		return removed;
	}
}
