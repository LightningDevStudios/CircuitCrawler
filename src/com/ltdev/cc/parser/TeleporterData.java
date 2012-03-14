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

		teleporterRef.setTexture(tex);
		
		entData.add(teleporterRef);
		ent = teleporterRef;
	}
}
