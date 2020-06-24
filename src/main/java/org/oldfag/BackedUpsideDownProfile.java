package org.oldfag;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;

/**
 * @author cookiedragon234 06/Jun/2020
 */
public class BackedUpsideDownProfile extends GameProfile {
	final GameProfile original;
	public BackedUpsideDownProfile(GameProfile original) {
		super(original.getId(), original.getName());
		this.original = original;
	}
	
	@Override
	public String getName() {
		//return "Dinnerbone";
		return super.getName();
	}
	
	@Override
	public PropertyMap getProperties() {
		return super.getProperties();
		//return original.getProperties();
	}
	
	@Override
	public boolean isComplete() {
		return original.isComplete();
	}
	
	@Override
	public boolean isLegacy() {
		return original.isLegacy();
	}
}
