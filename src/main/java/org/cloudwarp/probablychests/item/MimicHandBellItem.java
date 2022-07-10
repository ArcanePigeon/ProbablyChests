package org.cloudwarp.probablychests.item;

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
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
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
}
