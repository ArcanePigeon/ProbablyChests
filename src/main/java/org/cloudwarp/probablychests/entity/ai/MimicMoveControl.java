package org.cloudwarp.probablychests.entity.ai;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;

public class MimicMoveControl extends MoveControl {
	private final PCTameablePetWithInventory mimic;
	private float targetYaw;
	private int ticksUntilJump;
	private boolean jumpOften;

	public MimicMoveControl (PCTameablePetWithInventory mimic) {
		super(mimic);
		this.mimic = mimic;
		this.targetYaw = 180.0F * mimic.getYaw() / 3.1415927F;
	}

	public void look (float targetYaw, boolean jumpOften) {
		this.targetYaw = targetYaw;
		this.jumpOften = jumpOften;
	}

	public void move (double speed) {
		this.speed = speed;
		this.state = State.MOVE_TO;
	}

	public void tick () {
		this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
		this.entity.headYaw = this.entity.getYaw();
		this.entity.bodyYaw = this.entity.getYaw();
		if (this.state != State.MOVE_TO) {
			this.entity.setForwardSpeed(0.0F);
		} else {
			this.state = State.WAIT;
			if (this.entity.isOnGround()) {
				this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				if (this.ticksUntilJump-- <= 0) {
					this.ticksUntilJump = this.mimic.getTicksUntilNextJump();
					if (this.jumpOften) {
						this.ticksUntilJump /= 3;
					}

					this.mimic.getJumpControl().setActive();
					this.mimic.playSound(this.mimic.getJumpSound(), this.mimic.getSoundVolume(), this.mimic.getJumpSoundPitch());
				} else {
					this.mimic.sidewaysSpeed = 0.0F;
					this.mimic.forwardSpeed = 0.0F;
					this.entity.setMovementSpeed(0.0F);
				}
			} else {
				this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
			}

		}
	}
}