package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import org.cloudwarp.probablychests.registry.PCVoxelShapes;

public class NormalPot extends PCPot {
	public NormalPot () {
		super(FabricBlockSettings.of(Material.GLASS)
				.hardness(1.0F)
				.resistance(1.0F)
				.sounds(BlockSoundGroup.BONE)
				.breakInstantly()
				.nonOpaque(), PCVoxelShapes.POT_VOXELSHAPE);
	}
}
