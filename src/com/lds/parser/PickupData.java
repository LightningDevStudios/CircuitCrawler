package com.lds.parser;

import java.util.HashMap;

public class PickupData extends PhysEntData
{
	protected int value;
	public PickupData(HashMap<String, String> pickupHM)
	{
		super(pickupHM);
		
		if(pickupHM.get("value") != null)
			value = Integer.parseInt(pickupHM.get("value"));
		else
			value = 100;
	}
}
