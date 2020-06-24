package org.oldfag;

import net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.SoundEffects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

/**
 * @author cookiedragon234 07/Jun/2020
 */
public class SoundManager implements Listener {
	private static Field soundEffectA;
	private static final SoundEffect[] soundEffects = new SoundEffect[] {
		SoundEffects.X, // Cat purr
		SoundEffects.ik, // Villager ambient
		SoundEffects.im, // Villager hurt,
		SoundEffects.hz, // Spider ambient
		SoundEffects.cx, // Guardian on land
		SoundEffects.ix, // Witch ambient
		SoundEffects.iv, // Rain
		SoundEffects.hy, // Snow step
		// Actually disable these, these are too annoying rofl
		/*SoundEffects.ce, // Ghast scream
		SoundEffects.cg, // Ghast warn
		SoundEffects.a, // Ambient cave
		SoundEffects.aY, // Enderdragon growl
		SoundEffects.bg, // Endermen scream
		SoundEffects.bh, // Endermen stare*/
	};
	private static int currentSoundEffect = 0;
	
	private static SoundEffect nextSoundEffect() {
		return soundEffects[currentSoundEffect++ % soundEffects.length];
	}
	
	public static void onSoundEffect(PacketPlayOutNamedSoundEffect packet) {
		if (soundEffectA == null) {
			try {
				soundEffectA = PacketPlayOutNamedSoundEffect.class.getDeclaredField("a");
				soundEffectA.setAccessible(true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (soundEffectA != null) {
			try {
				soundEffectA.set(packet, nextSoundEffect());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
