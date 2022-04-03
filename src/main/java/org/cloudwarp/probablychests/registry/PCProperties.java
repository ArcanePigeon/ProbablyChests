package org.cloudwarp.probablychests.registry;

import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.state.property.EnumProperty;
import org.cloudwarp.probablychests.utils.PCChestState;

public class PCProperties {
	public static final EnumProperty<PCChestState> PC_CHEST_STATE = EnumProperty.of("pc_chest_state", PCChestState.class);
}
