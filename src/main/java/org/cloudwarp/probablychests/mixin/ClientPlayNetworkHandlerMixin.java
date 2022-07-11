package org.cloudwarp.probablychests.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.NetworkThreadUtils;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.ClientPlayPacketListenerAccess;
import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;
import org.cloudwarp.probablychests.screen.PCMimicScreen;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListenerAccess {
	@Final
	@Shadow private MinecraftClient client;
	@Shadow private ClientWorld world;
	@Override
	public void onOpenMimicScreen(OpenMimicScreenS2CPacket packet) {
		/*NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler)(Object)this, this.client);
		Entity entity = this.world.getEntityById(packet.getMimicId());
		if (entity instanceof PCTameablePetWithInventory mimicEntity) {
			ClientPlayerEntity clientPlayerEntity = this.client.player;
			SimpleInventory simpleInventory = new SimpleInventory(packet.getSlotCount());
			PCMimicScreenHandler mimicScreenHandler = new PCMimicScreenHandler(packet.getSyncId(), clientPlayerEntity.getInventory(), simpleInventory, mimicEntity);
			clientPlayerEntity.currentScreenHandler = mimicScreenHandler;
			this.client.setScreen(new PCMimicScreen(mimicScreenHandler, clientPlayerEntity.getInventory(), mimicEntity));
		}*/
	}
}
