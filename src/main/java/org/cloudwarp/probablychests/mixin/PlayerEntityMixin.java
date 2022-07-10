package org.cloudwarp.probablychests.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {
	@Shadow
	public PlayerScreenHandler playerScreenHandler;
	@Shadow public ScreenHandler currentScreenHandler;

	protected PlayerEntityMixin (EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow public PlayerInventory getInventory() {
		return null;
	}
	public void openMimicInventory(PCTameablePetWithInventory horse, Inventory inventory) {
	}
}
