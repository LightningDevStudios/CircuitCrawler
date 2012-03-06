package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.Teleporter;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class TeleporterData extends StaticEntData
{
	private Teleporter teleporterRef;
	
	public TeleporterData(HashMap<String, String> teleporterHM)
	{
		super(teleporterHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		teleporterRef = new Teleporter(size, new Vector2(xPos, yPos));

		//COLOR
		if (color != null)
			teleporterRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		teleporterRef.setTexture(tex);
		
		entData.add(teleporterRef);
		ent = teleporterRef;
	}
}
