package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;

import com.lds.game.entity.PickupHealth;

public class PickupHealthData extends PickupData
{
	private PickupHealth pickupHealthRef;
	
	private int pickupHealth;
	
	private int healthValue;
	public PickupHealthData(HashMap<String, String> pickupHealthHM)
	{
		super(pickupHealthHM);
		
		pickupHealth = Integer.parseInt(pickupHealthHM.get("healthValue"));
	}
	
	public void setHealthValue(int newHealthValue)		{healthValue = newHealthValue;}
	public int getHealthValue()		{return healthValue;}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupHealthRef = new PickupHealth(healthValue, xPos, yPos);
		if (rgba != null)
			pickupHealthRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			pickupHealthRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(pickupHealthRef);
	}
}
