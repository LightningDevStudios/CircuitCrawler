package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Ball;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysBallData extends HoldObjectData
{
	private Ball physBallRef;
	public PhysBallData(HashMap<String, String> physCircleHM)
	{
		super(physCircleHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		physBallRef = new Ball(size, new Vector2(xPos, yPos));

		physBallRef.setTexture(tex);
		
		entData.add(physBallRef);
		ent = physBallRef;
	}
}
