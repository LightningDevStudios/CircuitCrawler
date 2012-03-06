package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Cannon;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class CannonData extends StaticEntData
{
	private float speed;
	private int time;
	private Cannon cannonRef;
	
	public CannonData(HashMap<String, String> cannonHM)
	{
		super(cannonHM);
		
		if (cannonHM.get("speed") != null)
			speed = Float.parseFloat(cannonHM.get("speed"));
		
		if (cannonHM.get("time") != null)
			time = Integer.parseInt(cannonHM.get("time"));
	}
	
	/**
	 * \bug Cannons will crash the game. Restructure the parser to fix this.
	 */
	public void createInst(ArrayList<Entity> entData)
	{
		cannonRef = new Cannon(size, new Vector2(xPos, yPos), angle, speed, time, null);
		
		//COLOR
		if (color != null)
			cannonRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		cannonRef.setTexture(tex);
		
		entData.add(cannonRef);
		ent = cannonRef;
	}
}
