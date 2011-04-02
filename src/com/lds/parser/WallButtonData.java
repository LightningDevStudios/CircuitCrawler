package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.Entity;
import com.lds.game.entity.WallButton;

public class WallButtonData extends StaticEntData
{
	private WallButton wallButtonRef;
	public WallButtonData(HashMap<String, String> wbdHM)
	{
		super(wbdHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		wallButtonRef = new WallButton(xPos, yPos, angle);
		
		//COLOR
		if (color != null)
			wallButtonRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			wallButtonRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			wallButtonRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			wallButtonRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(wallButtonRef);
		ent = wallButtonRef;
	}
}
