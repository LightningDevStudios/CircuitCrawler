package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;
import com.lds.game.entity.Entity;
import com.lds.game.entity.Teleporter;

public class TeleporterData extends StaticEntData
{
	private Teleporter teleporterRef;
	
	public TeleporterData(HashMap<String, String> teleporterHM)
	{
		super(teleporterHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		teleporterRef = new Teleporter(size, xPos, yPos);

		//COLOR
		if (color != null)
			teleporterRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
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
