package org.cloudwarp.probablychests.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record PCPotFeatureConfig(FloatProvider mimicChance) implements FeatureConfig {
	public static final Codec<PCPotFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			FloatProvider.VALUE_CODEC.fieldOf("mimicChance").forGetter(PCPotFeatureConfig::mimicChance)
	).apply(instance, instance.stable(PCPotFeatureConfig::new)));
}
