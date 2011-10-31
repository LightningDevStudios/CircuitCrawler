package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.PickupHealth;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PickupHealthData extends PickupData
{
	private PickupHealth pickupHealthRef;
	
	
	public PickupHealthData(HashMap<String, String> pickupHealthHM)
	{
		super(pickupHealthHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		pickupHealthRef = new PickupHealth(new Vector2(xPos, yPos), value);
		
		//COLOR
		if (color != null)
			pickupHealthRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		pickupHealthRef.setTexture(tex);
		
		entData.add(pickupHealthRef);
		ent = pickupHealthRef;
	}
}
