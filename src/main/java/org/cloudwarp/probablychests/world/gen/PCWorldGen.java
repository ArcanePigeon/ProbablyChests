package org.cloudwarp.probablychests.world.gen;

public class PCWorldGen {
	public static void generatePCWorldGen () {
		PCChestGeneration.generateChest();
		PCNormalPotGeneration.generatePot();
	}
}
