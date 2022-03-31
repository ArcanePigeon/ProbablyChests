package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class LushChestBlock extends PCChestBlock{

	public LushChestBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
						.hardness(2.0F)
						.resistance(2.0F)
						.sounds(BlockSoundGroup.WOOD)
						.requiresTool(),
				PCChestTypes.LUSH);
	}
}
