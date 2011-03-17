package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;

import com.lds.game.entity.PickupHealth;

public class PickupHealthData extends PickupData
{
	private PickupHealth pickupHealthRef;
	
	
	public PickupHealthData(HashMap<String, String> pickupHealthHM)
	{
		super(pickupHealthHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupHealthRef = new PickupHealth(value, xPos, yPos);
		
		//COLOR
		if (color != null)
			pickupHealthRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			pickupHealthRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			pickupHealthRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			pickupHealthRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(pickupHealthRef);
		ent = pickupHealthRef;
	}
}
