package org.cloudwarp.probablychests.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;

public enum PCChestState implements StringIdentifiable {
	OPEN("open"),
	OPENED("opened"),
	CLOSE("close"),
	CLOSED("closed");

	private final String name;


	private PCChestState(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}
}
