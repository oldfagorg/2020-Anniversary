package org.oldfag;

import net.minecraft.server.v1_12_R1.WorldMap;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @author cookiedragon234 06/Jun/2020
 */
public class MapManager {
	static BufferedImage img = null;
	
	static {
		try {
			img = ImageIO.read(OldfagAnniversary.class.getResourceAsStream("/epic.png"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void onMapCreate(WorldMap map) {
		MapView view = map.mapView;
		if (img == null || view == null) return;
		for (MapRenderer renderer : view.getRenderers()) {
			view.removeRenderer(renderer);
		}
		MapRenderer customRenderer = new MapRenderer() {
			@Override
			public void render(MapView map, MapCanvas canvas, Player player) {
				map.setScale(MapView.Scale.NORMAL);
				canvas.drawImage(5, 5, img);
			}
		};
		view.addRenderer(customRenderer);
	}
}
