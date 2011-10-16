package com.lds.parser;

import com.lds.game.entity.Door;
import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

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
		doorRef.setScale(new Vector2(xScl, yScl));
		
		//COLOR
		if (color != null)
			doorRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
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
