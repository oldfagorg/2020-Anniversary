package org.oldfag;

import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.CharStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Credit to https://gist.github.com/aadnk/0502e32369f203daaba9
 */
public class SkinManager {
	private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	
	private static LoadingCache<String, String> profileCache = CacheBuilder.newBuilder().
		maximumSize(500).
		expireAfterWrite(4, TimeUnit.HOURS).
		build(new CacheLoader<String, String>() {
			public String load(String name) throws Exception {
				return getProfileJson(name);
			};
		});
	
	public static void updateSkin(GameProfile profile, String skinOwner) {
		try {
			String jsonStr = profileCache.get(skinOwner);
			JSONObject json = (JSONObject) new JSONParser().parse(jsonStr);
			JSONArray properties = (JSONArray) json.get("properties");
			for (Object o : properties) {
				JSONObject property = (JSONObject) o;
				String name = (String) property.get("name");
				String value = (String) property.get("value");
				String signature = (String) property.get("signature"); // May be NULL
				
				profile.getProperties().removeAll(name);
				if (signature == null) {
					profile.getProperties().put(name, new Property(name, value));
				} else {
					profile.getProperties().put(name, new Property(name, value, signature));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot fetch profile for " + skinOwner, e);
		}
	}
	
	private static String getProfileJson(String name) throws IOException {
		final URL url = new URL(PROFILE_URL + name + "?unsigned=false");
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 4.01; Windows NT)");
		return IOUtils.toString(conn.getInputStream());
	}
	
	@SuppressWarnings("deprecation")
	private static UUID extractUUID(final String playerName) {
		return Bukkit.getOfflinePlayer(playerName).getUniqueId();
	}
}
