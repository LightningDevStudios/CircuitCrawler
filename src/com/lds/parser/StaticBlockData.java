package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;

import com.lds.game.entity.StaticBlock;

public class StaticBlockData extends StaticEntData
{
	private StaticBlock staticBlockRef;
	
	public StaticBlockData(HashMap<String, String> staticBlockHM)
	{
		super(staticBlockHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		staticBlockRef = new StaticBlock(size, xPos, yPos);
		staticBlockRef.setAngle(angle);

		//COLOR
		if (color != null)
			staticBlockRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			staticBlockRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			staticBlockRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			staticBlockRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(staticBlockRef);
		ent = staticBlockRef;
	}
}
