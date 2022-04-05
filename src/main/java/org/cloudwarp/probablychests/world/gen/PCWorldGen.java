package org.cloudwarp.probablychests.world.gen;

public class PCWorldGen {
	public static void generatePCWorldGen () {
		PCLushChestGeneration.generateChest();
		PCNormalPotGeneration.generatePot();
	}
}
