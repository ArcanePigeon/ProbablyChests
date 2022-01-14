package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class EntityUtils {

    public static Optional<BlockPos> findNearestScarecrow(World world, LivingEntity entity, Tag.Identified<Block> tag) {
        return BlockPos.findClosest(entity.getBlockPos(), 15, 10, (blockPos) -> world.getBlockState(blockPos).isIn(tag));
    }

    public static boolean isScarecrowAround(LivingEntity entity, BlockPos pos) {
        return pos != null && entity.getBlockPos().isWithinDistance(pos, 9.0D);
    }
}
