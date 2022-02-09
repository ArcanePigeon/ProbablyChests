package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cloudwarp.mobscarecrow.MobScarecrow;

import java.util.Optional;

public class EntityUtils {

	public static Optional<BlockPos> findNearestScarecrow (World world, LivingEntity entity, Tag.Identified<Block> tag) {
		return BlockPos.findClosest(entity.getBlockPos(), 7 + MobScarecrow.mobScarecrowRadius, 10, (blockPos) -> world.getBlockState(blockPos).isIn(tag));
	}

	public static boolean isScarecrowAround (LivingEntity entity, BlockPos pos) {
		// Due to problem with mojang's distance method the 1D+ will need to be removed in the next update.
		return pos != null && entity.getBlockPos().isWithinDistance(pos, 1D + MobScarecrow.mobScarecrowRadius);
	}
}
