package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;
import com.lds.game.entity.Door;
import com.lds.game.entity.Entity;

public class DoorData extends StaticEntData
{
	private Door doorRef;
	
	public DoorData(HashMap<String, String> doorHM)
	{
		super(doorHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		doorRef = new Door(xPos, yPos);
		doorRef.setAngle(angle);
		doorRef.setXScl(xScl);
		doorRef.setYScl(yScl);
		
		//COLOR
		if (color != null)
			doorRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			doorRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			doorRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			doorRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(doorRef);
		ent = doorRef;
	}
}