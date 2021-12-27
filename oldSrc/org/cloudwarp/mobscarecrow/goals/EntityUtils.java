package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class EntityUtils {

    public static Optional<BlockPos> findNearestScarecrow(World world, LivingEntity entity, Tag.Identified<Block> tag) {
        return BlockPos.findClosest(entity.getBlockPos(), 8, 4, (blockPos) -> world.getBlockState(blockPos).isIn(tag));
    }

    public static boolean isScarecrowAround(LivingEntity entity, Optional<BlockPos> pos) {
        return pos.isPresent() && pos.get().isWithinDistance(pos.get(), 8.0D);
    }
}
