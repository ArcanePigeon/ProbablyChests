package org.cloudwarp.probablychests.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;

public enum PCPotTypes {
	LUSH(new Identifier(ProbablyChests.MOD_ID, "lush_pot")),
	NORMAL(new Identifier(ProbablyChests.MOD_ID, "normal_pot")),
	ROCKY(new Identifier(ProbablyChests.MOD_ID, "rocky_pot")),
	NETHER(new Identifier(ProbablyChests.MOD_ID, "nether_pot"));

	public final Identifier texture;

	PCPotTypes (Identifier texture) {

		this.texture = texture;

	}
	public FabricBlockSettings setting() {
		return switch (this) {
			case LUSH, NORMAL -> FabricBlockSettings.of(Material.GLASS)
					.hardness(1.0F)
					.resistance(1.0F)
					.sounds(BlockSoundGroup.BONE)
					.breakInstantly()
					.nonOpaque();
			case ROCKY, NETHER -> FabricBlockSettings.of(Material.GLASS)
					.hardness(1.0F)
					.resistance(1.0F)
					.sounds(BlockSoundGroup.BASALT)
					.breakInstantly()
					.nonOpaque();
			default -> FabricBlockSettings.of(Material.WOOD);
		};
	}
}
