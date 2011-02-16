package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.PhysCircle;
import com.lds.game.entity.Entity;

public class PhysCircleData extends HoldObjectData
{
	private PhysCircle physCircleRef;
	public PhysCircleData(HashMap<String, String> physCircleHM)
	{
		super(physCircleHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		physCircleRef = new PhysCircle(size, xPos, yPos);
		if (rgba != null)
			physCircleRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			physCircleRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(physCircleRef);
	}
}
