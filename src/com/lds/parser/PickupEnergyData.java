package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;

import com.lds.game.entity.PickupEnergy;

public class PickupEnergyData extends PickupData
{
	PickupEnergy pickupEnergyRef;
	
	private int energyValue;
	
	public PickupEnergyData(HashMap<String, String> pickupEnergyHM)
	{
		super(pickupEnergyHM);
		
		energyValue = Integer.parseInt(pickupEnergyHM.get("energyValue"));
	}
	
	public void setEnergyValue(int newEnergyValue)		{energyValue = newEnergyValue;}
	public int getEnergyValue()		{return energyValue;}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupEnergyRef = new PickupEnergy(energyValue, xPos, yPos);

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
	}
}
