package com.lds.parser;

import com.lds.game.entity.Cannon;
import com.lds.game.entity.Entity;

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
		cannonRef = new Cannon(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide, speed, 5);
		
		//COLOR
		if (color != null)
			cannonRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			cannonRef.enableGradientMode(gradient);
		
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
