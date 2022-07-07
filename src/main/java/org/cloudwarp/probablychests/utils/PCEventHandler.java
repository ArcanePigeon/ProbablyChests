package org.cloudwarp.probablychests.utils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCEventHandler {
	private PCEventHandler (){}

	public static void registerEvents(){
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			PacketByteBuf data = PacketByteBufs.create();
			data.writeNbt(ProbablyChests.configToNBT());
			ServerPlayNetworking.send(handler.player, ProbablyChests.id("probably_chests_config_update"), data);
		});
	}
}
