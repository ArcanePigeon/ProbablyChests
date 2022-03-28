package org.cloudwarp.probablychests.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.blockdetails.MobScarecrowBlockTags;
import org.cloudwarp.probablychests.goals.AvoidScarecrowGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonEntityMixin extends MobEntity {

	protected SkeletonEntityMixin (EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "initGoals()V")
	private void mixinInitGoals (CallbackInfo ci) {
		this.goalSelector.add(0, new AvoidScarecrowGoal((AbstractSkeletonEntity) (Object) this, MobScarecrowBlockTags.SKELETON_SCARECROW));
		this.goalSelector.add(0, new AvoidScarecrowGoal((AbstractSkeletonEntity) (Object) this, MobScarecrowBlockTags.MOB_SCARECROW));
	}
}
