package org.cloudwarp.probablychests.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.registry.PCBlockEntityTypes;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public class PCChestBlockEntity extends ChestBlockEntity {

	PCChestTypes type;
	int viewerCount = 0;
	private float animationAngle;
	private float lastAnimationAngle;

	public PCChestBlockEntity (PCChestTypes type, BlockPos blockPos, BlockState blockState) {
		super(type.getBlockEntityType(), blockPos, blockState);
		this.type = type;
	}

	public PCChestBlockEntity (BlockPos blockPos, BlockState blockState) {
		super(PCBlockEntityTypes.PC_CHEST_BLOCK_ENTITY_TYPE, blockPos, blockState);
	}

	@Override
	protected ScreenHandler createScreenHandler (int syncId, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	@Override
	public ScreenHandler createMenu (int syncId, PlayerInventory inventory, PlayerEntity player) {
		return new PCScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
	}

	@Override
	protected Text getContainerName () {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	public int size () {
		return type.size;
	}

	public PCChestTypes type () {
		return type;
	}

	@Environment(EnvType.CLIENT)
	public int countViewers () {
		return viewerCount;
	}

	public void onOpen (PlayerEntity player) {
		if (! player.isSpectator()) {
			++ this.viewerCount;
			sync();
		}
	}

	public void onClose (PlayerEntity player) {
		if (! player.isSpectator()) {
			-- this.viewerCount;
			sync();
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getAnimationProgress (float f) {
		return MathHelper.lerp(f, lastAnimationAngle, animationAngle);
	}

	@Environment(EnvType.CLIENT)
	public void clientTick () {
		if (world != null && world.isClient) {
			int viewerCount = countViewers();
			lastAnimationAngle = animationAngle;
			if (viewerCount > 0 && animationAngle == 0.0F) {
				playSound(SoundEvents.BLOCK_CHEST_OPEN);
			}
			if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F) {
				float float_2 = animationAngle;
				if (viewerCount > 0) {
					animationAngle += 0.1F;
				} else {
					animationAngle -= 0.1F;
				}
				animationAngle = MathHelper.clamp(animationAngle, 0, 1);
				if (animationAngle < 0.5F && float_2 >= 0.5F) {
					playSound(SoundEvents.BLOCK_CHEST_CLOSE);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	private void playSound (SoundEvent soundEvent) {
		double d = (double) this.pos.getX() + 0.5D;
		double e = (double) this.pos.getY() + 0.5D;
		double f = (double) this.pos.getZ() + 0.5D;
		this.world.playSound(d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F, false);
	}

}
