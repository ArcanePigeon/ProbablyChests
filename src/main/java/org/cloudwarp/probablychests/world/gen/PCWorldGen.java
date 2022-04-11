package org.cloudwarp.probablychests.world.gen;

public class PCWorldGen {
	public static void generatePCWorldGen () {
		LushChestGeneration.generateChest();
		NormalChestGeneration.generateChest();
		RockyChestGeneration.generateChest();
		StoneChestGeneration.generateChest();
		GoldChestGeneration.generateChest();
		PCPotGeneration.generatePot();
	}
}
