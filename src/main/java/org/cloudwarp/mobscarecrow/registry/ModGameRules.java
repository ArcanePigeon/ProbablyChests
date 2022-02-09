package org.cloudwarp.mobscarecrow.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.cloudwarp.mobscarecrow.MobScarecrow;
import org.cloudwarp.mobscarecrow.networking.MobScarecrowNetworking;

import java.util.List;

public class ModGameRules {
	public static final GameRules.Key<GameRules.IntRule> MOB_SCARECROW_RADIUS_RULE =
			GameRuleRegistry.register("mobScarecrowRadius", GameRules.Category.MISC, GameRuleFactory.createIntRule(8, 0, 100, (server, rule) -> {
				List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
				PacketByteBuf buffer = new PacketByteBuf(PacketByteBufs.create().writeInt(rule.get()));
				MobScarecrow.mobScarecrowRadius = rule.get();
				for (ServerPlayerEntity player : players) {
					ServerPlayNetworking.send(player, MobScarecrowNetworking.MOB_SCARECROW_RADIUS, buffer);
				}
			}));

	public static void init () {
	}
}
