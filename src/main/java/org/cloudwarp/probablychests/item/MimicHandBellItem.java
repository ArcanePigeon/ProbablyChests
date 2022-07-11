package org.cloudwarp.probablychests.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MimicHandBellItem extends Item {
	public MimicHandBellItem (Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock (ItemUsageContext context) {

		World world = context.getWorld();
		ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
		BlockPos blockPos = itemPlacementContext.getBlockPos();

		if (world instanceof ServerWorld serverWorld && context.getPlayer() != null) {
			blockPos = blockPos.offset(context.getSide().getOpposite());
			if(serverWorld.getBlockState(blockPos).isOf(Blocks.AMETHYST_CLUSTER)){
				((PlayerEntityAccess) context.getPlayer()).abandonMimics();
				playSound(world,blockPos,PCSounds.BELL_HIT_1);
			}
		}
		return ActionResult.success(world.isClient);
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if(Screen.hasShiftDown()){
			tooltip.add(new TranslatableText("item.probablychests.mimicHandBell.tooltip.shift"));
			tooltip.add(new TranslatableText("item.probablychests.mimicHandBell.tooltip.shift2"));
			tooltip.add(new TranslatableText("item.probablychests.mimicHandBell.tooltip.shift3"));
		}else{
			tooltip.add(new TranslatableText("item.probablychests.shift.tooltip"));
		}
	}
	static void playSound (World world, BlockPos pos, SoundEvent soundEvent) {
		double d = (double) pos.getX() + 0.5;
		double e = (double) pos.getY() + 0.5;
		double f = (double) pos.getZ() + 0.5;

		world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.8f, world.random.nextFloat() * 0.1f + 0.9f);
	}
}
