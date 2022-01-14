package org.cloudwarp.mobscarecrow.goals;

import net.minecraft.block.Block;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.Tag;
import org.cloudwarp.mobscarecrow.registry.ModBlocks;

public class StepOnScarecrowGoal extends StepOnScarecrowBlockGoal {
    public StepOnScarecrowGoal(PathAwareEntity mob, double speed, int maxYDifference, Tag.Identified<Block> tag) {
        super(tag, mob, speed, maxYDifference);
    }

    public double getDesiredDistanceToTarget() {
        return 1.14D;
    }
}
