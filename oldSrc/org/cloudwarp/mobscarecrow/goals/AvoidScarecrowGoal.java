package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.Optional;

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
        Vec3d newPos = NoPenaltyTargeting.find(pathEntity,9,7,scarecrowPos);
        if(newPos != null){
            path = navigation.findPathTo(new BlockPos(newPos),0);
            if(path != null){
                pathTimeLimiter = 5;
                return true;
            }
        }
        return false;
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
