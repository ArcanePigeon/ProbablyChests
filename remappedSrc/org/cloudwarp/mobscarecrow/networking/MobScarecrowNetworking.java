package org.cloudwarp.mobscarecrow.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.cloudwarp.mobscarecrow.MobScarecrow;
import org.cloudwarp.mobscarecrow.registry.ModGameRules;

public class MobScarecrowNetworking {
	public static final Identifier MOB_SCARECROW_RADIUS = MobScarecrow.id("mob_scarecrow_radius");

	public static void init () {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(MOB_SCARECROW_RADIUS, new PacketByteBuf(PacketByteBufs.create().writeInt(server.getGameRules().getInt(ModGameRules.MOB_SCARECROW_RADIUS_RULE))));
		});
	}
}
