package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.Spike;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class SpikeData extends StaticEntData
{
	private Spike spikeRef;
	public SpikeData(HashMap<String, String> sdHM)
	{
		super(sdHM);
	}

	public void createInst(ArrayList<Entity> entData)
	{
		spikeRef = new Spike(size, new Vector2(xPos, yPos), angle);
		
		//COLOR
		if (color != null)
			spikeRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		spikeRef.setTexture(tex);
		
		entData.add(spikeRef);
		ent = spikeRef;
	}
}
