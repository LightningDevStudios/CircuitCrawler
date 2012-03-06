package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.WallButton;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class WallButtonData extends StaticEntData
{
	private WallButton wallButtonRef;
	public WallButtonData(HashMap<String, String> wbdHM)
	{
		super(wbdHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		wallButtonRef = new WallButton(new Vector2(xPos, yPos), angle);
		
		//COLOR
		if (color != null)
			wallButtonRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		wallButtonRef.setTexture(tex);
		
		entData.add(wallButtonRef);
		ent = wallButtonRef;
	}
}
