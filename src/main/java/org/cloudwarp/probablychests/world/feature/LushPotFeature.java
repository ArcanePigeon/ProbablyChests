package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.cloudwarp.probablychests.registry.PCBlocks;


public class LushPotFeature extends Feature<PCPotFeatureConfig> {
	public LushPotFeature (Codec<PCPotFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate (FeatureContext<PCPotFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos pos = context.getOrigin();
		PCPotFeatureConfig config = context.getConfig();
		structureWorldAccess.setBlockState(pos, PCBlocks.LUSH_POT.getDefaultState(), 3);
		return true;
	}
}
