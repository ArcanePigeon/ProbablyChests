package org.cloudwarp.mobscarecrow.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.cloudwarp.mobscarecrow.blockdetails.MobScarecrowBlockTags;
import org.cloudwarp.mobscarecrow.goals.AvoidScarecrowGoal;
import org.cloudwarp.mobscarecrow.goals.StepOnScarecrowGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends MobEntity {

	protected EndermanEntityMixin (EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "initGoals()V")
	private void mixinInitGoals (CallbackInfo ci) {
		this.goalSelector.add(0, new AvoidScarecrowGoal((EndermanEntity) (Object) this, MobScarecrowBlockTags.MOB_SCARECROW));
		this.goalSelector.add(2, new StepOnScarecrowGoal((EndermanEntity) (Object) this, 1.5D, 3, MobScarecrowBlockTags.ENDERMITE_SCARECROW));
	}
}
