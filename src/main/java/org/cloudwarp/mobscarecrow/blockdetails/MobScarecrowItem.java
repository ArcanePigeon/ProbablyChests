package org.cloudwarp.mobscarecrow.blockdetails;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobScarecrowItem extends BlockItem {
	public MobScarecrowItem (Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public void appendTooltip (ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		NbtCompound tag = stack.getSubNbt("BlockEntityTag");
		if (tag == null) {
			return;
		}
		// TODO: Add interesting tool tips for each item.
		//String name = tag.getString("waystone_name");
		//boolean global = tag.getBoolean("waystone_is_global");
        /*tooltip.add(new TranslatableText("waystones.waystone_tooltip.name", new TranslatableText("waystones.waystone_tooltip.name.arg_color").append(name)));
        tooltip.add(new TranslatableText("waystones.waystone_tooltip.global").append(" ")
                .append(new TranslatableText("waystones.waystone_tooltip.global_" + (global ? "on" : "off"))));*/
	}
}
