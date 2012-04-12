package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Block;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysBlockData extends HoldObjectData
{
	private Block physBlockRef;
	
	public PhysBlockData(HashMap<String, String> physBlockHM)
	{
		super(physBlockHM);
	}
	public void createInst(ArrayList<Entity> entData)
	{
		physBlockRef = new Block(size, new Vector2(xPos, yPos));
		physBlockRef.setAngle(angle);

		physBlockRef.setTexture(tex);
		
		entData.add(physBlockRef);
		ent = physBlockRef;
	}
}
