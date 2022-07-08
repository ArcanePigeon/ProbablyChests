package org.cloudwarp.probablychests.interfaces;

import org.cloudwarp.probablychests.network.packet.OpenMimicScreenS2CPacket;

public interface ClientPlayPacketListenerAccess {
	void onOpenMimicScreen(OpenMimicScreenS2CPacket var1);
}
