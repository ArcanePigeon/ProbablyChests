package org.cloudwarp.probablychests.network.packet;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.cloudwarp.probablychests.interfaces.ClientPlayPacketListenerAccess;

public class OpenMimicScreenS2CPacket implements Packet<ClientPlayPacketListener> {
	private final int syncId;
	private final int slotCount;
	private final int mimicId;

	public OpenMimicScreenS2CPacket(int syncId, int slotCount, int mimicId) {
		this.syncId = syncId;
		this.slotCount = slotCount;
		this.mimicId = mimicId;
	}

	public OpenMimicScreenS2CPacket(PacketByteBuf buf) {
		this.syncId = buf.readUnsignedByte();
		this.slotCount = buf.readVarInt();
		this.mimicId = buf.readInt();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeByte(this.syncId);
		buf.writeVarInt(this.slotCount);
		buf.writeInt(this.mimicId);
	}

	@Override
	public void apply(ClientPlayPacketListener clientPlayPacketListener) {
		((ClientPlayPacketListenerAccess)clientPlayPacketListener).onOpenMimicScreen(this);
	}

	public int getSyncId() {
		return this.syncId;
	}

	public int getSlotCount() {
		return this.slotCount;
	}

	public int getMimicId () {
		return this.mimicId;
	}
}

