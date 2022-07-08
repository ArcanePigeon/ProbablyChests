package org.cloudwarp.probablychests.mixin;

import net.minecraft.network.listener.ClientPlayPacketListener;
import org.cloudwarp.probablychests.interfaces.ClientPlayPacketListenerAccess;
import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayPacketListener.class)
public interface ClientPlayPacketListenerMixin extends ClientPlayPacketListenerAccess {
	void onOpenMimicScreen (OpenMimicScreenS2CPacket var1);
}
