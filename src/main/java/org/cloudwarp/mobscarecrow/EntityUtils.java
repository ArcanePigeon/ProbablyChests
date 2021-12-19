package org.cloudwarp.mobscarecrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class EntityUtils {

    public static Optional<BlockPos> findNearestScarecrow(World world, LivingEntity entity) {
        return BlockPos.findClosest(entity.getBlockPos(), 8, 4, (blockPos) -> {
            return world.getBlockState(blockPos).isIn(MobScarecrowBlockTags.MOB_SCARECROW);
        });
    }

    public static boolean isScarecrowAround(LivingEntity entity, Optional<BlockPos> pos) {
        return pos.isPresent() && pos.get().isWithinDistance(pos.get(), 8.0D);
    }
}
