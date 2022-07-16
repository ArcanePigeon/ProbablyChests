package org.cloudwarp.probablychests.registry;

import net.minecraft.state.property.EnumProperty;
import org.cloudwarp.probablychests.utils.PCChestState;
import org.cloudwarp.probablychests.utils.PCLockedState;

public class PCProperties {
	public static final EnumProperty<PCChestState> PC_CHEST_STATE = EnumProperty.of("pc_chest_state", PCChestState.class);
	public static final EnumProperty<PCLockedState> PC_LOCKED_STATE = EnumProperty.of("pc_locked_state", PCLockedState.class);
}
