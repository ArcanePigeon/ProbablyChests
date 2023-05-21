package org.cloudwarp.probablychests.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCStatistics {
	public static final Identifier MIMIC_ENCOUNTERS = ProbablyChests.id("mimic_encounters");
	public static final Identifier ABANDONED_MIMICS = ProbablyChests.id("abandoned_mimics");

	public static void init () {
		Registry.register(Registries.CUSTOM_STAT, "mimic_encounters", MIMIC_ENCOUNTERS);
		Stats.CUSTOM.getOrCreateStat(MIMIC_ENCOUNTERS,StatFormatter.DEFAULT);
		Registry.register(Registries.CUSTOM_STAT, "abandoned_mimics", ABANDONED_MIMICS);
		Stats.CUSTOM.getOrCreateStat(ABANDONED_MIMICS,StatFormatter.DEFAULT);
	}
}
