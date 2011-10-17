package com.lds.parser;

import com.lds.game.entity.Cannon;
import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

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
	
	public void createInst(ArrayList<Entity> entData)
	{
		cannonRef = new Cannon(size, new Vector2(xPos, yPos), angle, speed, 5.0f);
		
		//COLOR
		if (color != null)
			cannonRef.enableColorMode(color[0], color[1], color[2], color[3]);
				
		//TEXTURE
		if (textureModeEnabled)
			cannonRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			cannonRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(cannonRef);
		ent = cannonRef;
	}
}
