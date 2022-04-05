package org.cloudwarp.probablychests.registry;

import com.google.common.collect.Sets;
import net.minecraft.util.Identifier;

import java.util.Set;

public class PCLootTables {

	private static final Set<Identifier> PC_LOOT_TABLES = Sets.newHashSet();

	public static Identifier LUSH_CHEST;

	private static Identifier register (String id) {
		return PCLootTables.registerLootTable(new Identifier(id));
	}

	private static Identifier registerLootTable (Identifier id) {
		if (PC_LOOT_TABLES.add(id)) {
			return id;
		}
		throw new IllegalArgumentException(id + " is already a registered built-in loot table");
	}

	public static void init () {
		LUSH_CHEST = PCLootTables.register("probablychests:chests/default_pc_chests");
	}
}
