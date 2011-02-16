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
		if (rgba != null)
			pickupEnergyRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			pickupEnergyRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(pickupEnergyRef);
	}
}
