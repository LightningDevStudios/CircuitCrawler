package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Ball;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysBallData extends HoldObjectData
{
	private Ball physBallRef;
	public PhysBallData(HashMap<String, String> physCircleHM)
	{
		super(physCircleHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		physBallRef = new Ball(size, new Vector2(xPos, yPos));

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
