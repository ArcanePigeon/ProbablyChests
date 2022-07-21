package org.cloudwarp.probablychests.utils;

import net.minecraft.util.StringIdentifiable;

public enum PCLockedState  implements StringIdentifiable {
	LOCKED("locked"),
	UNLOCKED("unlocked");
	private final String name;

	private PCLockedState (String name) {
		this.name = name;
	}

	@Override
	public String asString () {
		return this.name;
	}
}
