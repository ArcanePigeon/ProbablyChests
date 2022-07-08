package org.cloudwarp.probablychests.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
}
