package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Door;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class DoorData extends StaticEntData
{
	private Door doorRef;
	
	public DoorData(HashMap<String, String> doorHM)
	{
		super(doorHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		doorRef = new Door(size, new Vector2(xPos, yPos));
		doorRef.setAngle(angle);
		
		doorRef.setTexture(tex);
		
		entData.add(doorRef);
		ent = doorRef;
	}
}
