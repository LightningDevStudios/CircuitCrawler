package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;

import com.lds.game.entity.PickupEnergy;

public class PickupEnergyData extends PickupData
{
	PickupEnergy pickupEnergyRef;
	
	
	public PickupEnergyData(HashMap<String, String> pickupEnergyHM)
	{
		super(pickupEnergyHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupEnergyRef = new PickupEnergy(value, xPos, yPos);

		//COLOR
		if (color != null)
			pickupEnergyRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			pickupEnergyRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			pickupEnergyRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			pickupEnergyRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(pickupEnergyRef);
		ent = pickupEnergyRef;
	}
}
