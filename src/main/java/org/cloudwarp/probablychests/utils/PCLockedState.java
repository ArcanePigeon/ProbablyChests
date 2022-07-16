package org.cloudwarp.probablychests.utils;

import net.minecraft.util.StringIdentifiable;

public enum PCLockedState  implements StringIdentifiable {
	LOCKED("pc_locked"),
	UNLOCKED("pc_unlocked");
	private final String name;

	private PCLockedState (String name) {
		this.name = name;
	}

	@Override
	public String asString () {
		return this.name;
	}
}
