package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.PhysBall;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysBallData extends HoldObjectData
{
	private PhysBall physBallRef;
	public PhysBallData(HashMap<String, String> physCircleHM)
	{
		super(physCircleHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		physBallRef = new PhysBall(size, xPos, yPos, friction);

		//COLOR
		if (color != null)
			physBallRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			physBallRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			physBallRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			physBallRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(physBallRef);
		ent = physBallRef;
	}
}
