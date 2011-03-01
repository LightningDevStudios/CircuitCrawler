package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.PhysCircle;
import com.lds.game.entity.Entity;

public class PhysCircleData extends HoldObjectData
{
	private PhysCircle physCircleRef;
	public PhysCircleData(HashMap<String, String> physCircleHM)
	{
		super(physCircleHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		physCircleRef = new PhysCircle(size, xPos, yPos);

		//COLOR
		if (color != null)
			physCircleRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			physCircleRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			physCircleRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			physCircleRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(physCircleRef);
	}
}
