package org.cloudwarp.probablychests.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCSounds {
	public static final Identifier BELL_HIT_1_ID = ProbablyChests.id("bell_hit1");
	public static final Identifier BELL_HIT_2_ID = ProbablyChests.id("bell_hit2");
	public static final Identifier BELL_HIT_4_ID = ProbablyChests.id("bell_hit4");
	public static final Identifier CLOSE_2_ID = ProbablyChests.id("close2");
	public static final Identifier MIMIC_BITE_ID = ProbablyChests.id("mimic_bite");
	public static final Identifier APPLY_LOCK_1_ID = ProbablyChests.id("apply_lock1");
	public static final Identifier APPLY_LOCK_2_ID = ProbablyChests.id("apply_lock2");
	public static final Identifier LOCK_UNLOCK_ID = ProbablyChests.id("lock_unlock");
	public static SoundEvent BELL_HIT_1 = new SoundEvent(BELL_HIT_1_ID);
	public static SoundEvent BELL_HIT_2 = new SoundEvent(BELL_HIT_2_ID);
	public static SoundEvent BELL_HIT_4 = new SoundEvent(BELL_HIT_4_ID);
	public static SoundEvent CLOSE_2  = new SoundEvent(CLOSE_2_ID);
	public static SoundEvent MIMIC_BITE = new SoundEvent(MIMIC_BITE_ID);
	public static SoundEvent APPLY_LOCK1 = new SoundEvent(APPLY_LOCK_1_ID);
	public static SoundEvent APPLY_LOCK2 = new SoundEvent(APPLY_LOCK_2_ID);
	public static SoundEvent LOCK_UNLOCK = new SoundEvent(LOCK_UNLOCK_ID);

	public static void init () {
		Registry.register(Registry.SOUND_EVENT, BELL_HIT_1_ID, BELL_HIT_1);
		Registry.register(Registry.SOUND_EVENT, BELL_HIT_2_ID, BELL_HIT_2);
		Registry.register(Registry.SOUND_EVENT, BELL_HIT_4_ID, BELL_HIT_4);
		Registry.register(Registry.SOUND_EVENT, CLOSE_2_ID, CLOSE_2);
		Registry.register(Registry.SOUND_EVENT, MIMIC_BITE_ID, MIMIC_BITE);
		Registry.register(Registry.SOUND_EVENT, APPLY_LOCK_1_ID, APPLY_LOCK1);
		Registry.register(Registry.SOUND_EVENT, APPLY_LOCK_2_ID, APPLY_LOCK2);
		Registry.register(Registry.SOUND_EVENT, LOCK_UNLOCK_ID, LOCK_UNLOCK);
	}
}
