package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.SpikeBall;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class SpikeBallData extends PhysEntData
{
	protected float moveX, moveY, rotateDir;
	private SpikeBall spikeBallRef;
	
	public SpikeBallData(HashMap<String, String> sbdHM)
	{
		super(sbdHM);
		
		if (sbdHM.get("moveY") != null)
			moveY = Float.parseFloat(sbdHM.get("moveY"));
		if (sbdHM.get("moveX") != null)
			moveX = Float.parseFloat(sbdHM.get("moveY"));
		if (sbdHM.get("rotateDir") != null)
			rotateDir = Float.parseFloat(sbdHM.get("rotateDir"));
	}
	
	public  void createInst(ArrayList<Entity> entData)
	{
		spikeBallRef = new SpikeBall(size, new Vector2(xPos, yPos));
		
		//COLOR
		if (color != null)
			spikeBallRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		spikeBallRef.setTexture(tex);
		
		entData.add(spikeBallRef);
		ent = spikeBallRef;
	}
}
