package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.Entity;

import com.lds.game.entity.PhysBlock;

public class PhysBlockData extends HoldObjectData
{
	private PhysBlock physBlockRef;
	public PhysBlockData(HashMap<String, String> physBlockHM)
	{
		super(physBlockHM);
	}
	public void createInst(ArrayList<Entity> entData)
	{
		physBlockRef = new PhysBlock(size, xPos, yPos);

		//COLOR
		if (color != null)
			physBlockRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			physBlockRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			physBlockRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			physBlockRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(physBlockRef);
		ent = physBlockRef;
	}
}
