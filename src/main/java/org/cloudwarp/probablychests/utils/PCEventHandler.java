package org.cloudwarp.probablychests.utils;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

public class PCEventHandler {

	public static final Identifier MIMIC_INVENTORY_PACKET_ID= ProbablyChests.id("mimic_inventory_packet");
	private PCEventHandler (){}

	public static void registerEvents(){
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			PacketByteBuf data = PacketByteBufs.create();
			data.writeNbt(ProbablyChests.configToNBT());
			ServerPlayNetworking.send(handler.player, ProbablyChests.id("probably_chests_config_update"), data);
		});
	}
}
