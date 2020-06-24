package org.oldfag;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.ACCEPTED;
import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED;

/**
 * @author cookiedragon234 06/Jun/2020
 */
public class ResourcePackManager implements Listener {
	@EventHandler
	public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
		if (event.getStatus() != SUCCESSFULLY_LOADED && event.getStatus() != ACCEPTED) {
			event.getPlayer().kickPlayer("Please accept the resource pack");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player != null) {
			player.setResourcePack("https://i.binclub.dev/kb2gb4zc.zip");
		}
	}
}
