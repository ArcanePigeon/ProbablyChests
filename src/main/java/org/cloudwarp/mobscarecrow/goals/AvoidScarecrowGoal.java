package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class AvoidScarecrowGoal extends Goal {
    private LivingEntity entity;
    private float speed = 1.5F;
    /** The PathEntity of our entity */
    private Path path;
    /** The PathNavigate of our entity */
    private EntityNavigation navigation;
    private PathAwareEntity pathEntity;
    private boolean pathable = false;
    private Optional<BlockPos> pos;
    private int pathTimeLimiter;
    private Tag.Identified<Block> tag;

    public AvoidScarecrowGoal(LivingEntity e, Tag.Identified<Block> scarecrowTag){
        entity = e;
        tag = scarecrowTag;
        //speed = e.getMovementSpeed();
        if (entity instanceof PathAwareEntity) {
            pathable = true;
            pathEntity = (PathAwareEntity)entity;
            navigation = pathEntity.getNavigation();
        }
    }

    @Override
    public boolean shouldContinue(){
        return !path.isFinished() && pathTimeLimiter > 0;
    }

    @Override
    public boolean canStart() {
        pos = EntityUtils.findNearestScarecrow(entity.getEntityWorld(),entity,tag);
        if(pathable && EntityUtils.isScarecrowAround(entity,pos)){
            return generatePath();
        }
        return false;
    }

    private boolean generatePath(){
        Vec3d scarecrowPos = new Vec3d(pos.get().getX(),pos.get().getY(),pos.get().getZ());
        Vec3d currentPos = new Vec3d(entity.getX(),entity.getY(),entity.getZ());
        Vec3d newPos = NoPenaltyTargeting.find(pathEntity,9,7,scarecrowPos);
        //Vec3d newPos = find(pathEntity,9,7,currentPos.subtract(scarecrowPos).normalize());
        //System.out.println((scarecrowPos+ "|||||||" + currentPos+"|||||||||"+newPos+"||||||"+currentPos.subtract(scarecrowPos).normalize()));
        if(newPos != null){
            path = navigation.findPathTo(new BlockPos(newPos),0);
            if(path != null){
                pathTimeLimiter = 5;
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Vec3d find(PathAwareEntity entity, int horizontalRange, int verticalRange, Vec3d direction) {
        Vec3d vec3d = entity.getPos().subtract(direction);
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBestPathTarget(entity, () -> {
            BlockPos blockPos = localFuzz(entity.getRandom(), horizontalRange, verticalRange, 0, vec3d.x, vec3d.z, 0D);
            return blockPos == null ? null : tryMake(entity, horizontalRange, bl, blockPos);
        });
    }

    @Nullable
    private static BlockPos tryMake(PathAwareEntity entity, int horizontalRange, boolean posTargetInRange, BlockPos fuzz) {
        BlockPos blockPos = FuzzyPositions.towardTarget(entity, horizontalRange, entity.getRandom(), fuzz);
        return !NavigationConditions.isHeightInvalid(blockPos, entity) && !NavigationConditions.isPositionTargetOutOfWalkRange(posTargetInRange, entity, blockPos) && !NavigationConditions.isInvalidPosition(entity.getNavigation(), blockPos) && !NavigationConditions.hasPathfindingPenalty(entity, blockPos) ? blockPos : null;
    }

    @Nullable
    public static BlockPos localFuzz(Random random, int horizontalRange, int verticalRange, int startHeight, double directionX, double directionZ, double angleRange) {
        double d = MathHelper.atan2(directionZ, directionX) - 0D;
        double e = d + (double)(2.0F * 1 - 1.0F) * angleRange;
        double f = Math.sqrt(random.nextDouble()) * (double)MathHelper.SQUARE_ROOT_OF_TWO * (double)horizontalRange;
        double g = -f * Math.sin(e);
        double h = f * Math.cos(e);
        if (!(Math.abs(g) > (double)horizontalRange) && !(Math.abs(h) > (double)horizontalRange)) {
            int i = random.nextInt(2 * verticalRange + 1) - verticalRange + startHeight;
            return new BlockPos(g, (double)i, h);
        } else {
            return null;
        }
    }

    @Override
    public void start(){
        navigation.startMovingAlong(path,speed);
    }
    @Override
    public void tick(){
        pathTimeLimiter = Math.max(pathTimeLimiter - 1, 0);
    }
}
