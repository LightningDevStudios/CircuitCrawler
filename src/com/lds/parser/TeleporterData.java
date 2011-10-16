package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Teleporter;
import com.lds.math.Vector2;

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

		//COLOR
		if (color != null)
			teleporterRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			teleporterRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			teleporterRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			teleporterRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(teleporterRef);
		ent = teleporterRef;
	}
}
