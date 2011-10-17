package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.PickupEnergy;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PickupEnergyData extends PickupData
{
	PickupEnergy pickupEnergyRef;
	
	
	public PickupEnergyData(HashMap<String, String> pickupEnergyHM)
	{
		super(pickupEnergyHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupEnergyRef = new PickupEnergy(new Vector2(xPos, yPos), value);

		//COLOR
		if (color != null)
			pickupEnergyRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
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
