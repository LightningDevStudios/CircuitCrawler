package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;
import com.lds.game.entity.Door;
import com.lds.game.entity.Entity;

public class DoorData extends PhysEntData
{
	private Door doorRef;
	
	public DoorData(HashMap<String, String> doorHM)
	{
		super(doorHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		doorRef = new Door(xPos, yPos);
		if (rgba != null)
			doorRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			doorRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(doorRef);
	}
}