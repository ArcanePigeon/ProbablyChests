package org.cloudwarp.probablychests.utils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.cloudwarp.probablychests.ProbablyChests;
import static org.cloudwarp.probablychests.utils.MimicDifficulty.*;

@Config(name = ProbablyChests.MOD_ID)
public class PCConfig implements ConfigData {


	@ConfigEntry.Gui.CollapsibleObject
	public MimicSettings mimicSettings = new MimicSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public WorldGen worldGen = new WorldGen();

	@ConfigEntry.Gui.CollapsibleObject
	public ChestSettings chestSettings = new ChestSettings();

	public static class MimicSettings {
		@ConfigEntry.Gui.Tooltip()
		public MimicDifficulty mimicDifficulty = MEDIUM;
		@ConfigEntry.Gui.Tooltip()
		public boolean spawnNaturalMimics = true;
		@ConfigEntry.Gui.Tooltip()
		public float naturalMimicSpawnRate = 0.50F;
		@ConfigEntry.Gui.Tooltip()
		public boolean allowPetMimics = true;
		@ConfigEntry.Gui.Tooltip()
		public boolean doPetMimicLimit = false;
		@ConfigEntry.Gui.Tooltip()
		public int petMimicLimit = 2;
		@ConfigEntry.Gui.Tooltip()
		public int abandonedMimicTimer = 5;
		@ConfigEntry.Gui.Tooltip()
		public boolean allowPetMimicLocking = true;
	}

	public static class WorldGen {
		@ConfigEntry.Gui.Tooltip()
		public float potSpawnChance = 0.4F;
		@ConfigEntry.Gui.Tooltip()
		public float chestSpawnChance = 0.6F;
		@ConfigEntry.Gui.Tooltip()
		public float surfaceChestSpawnChance = 0.6F;
		@ConfigEntry.Gui.Tooltip()
		public float secretMimicChance = 0.20F;
	}

	public static class ChestSettings {
		@ConfigEntry.Gui.Tooltip()
		public boolean allowChestLocking = true;
		@ConfigEntry.Gui.Tooltip()
		public boolean enableLockedChestOwners = true;
	}


}
