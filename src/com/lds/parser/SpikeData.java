package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.Spike;
import com.lds.game.entity.Entity;

public class SpikeData extends StaticEntData
{
	private Spike spikeRef;
	public SpikeData(HashMap<String, String> sdHM)
	{
		super(sdHM);
	}

	public void createInst(ArrayList<Entity> entData)
	{
		spikeRef = new Spike(xPos, yPos, angle);
		spikeRef.setSize(size);
		
		//COLOR
		if (color != null)
			spikeRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			spikeRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			spikeRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			spikeRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(spikeRef);
		ent = spikeRef;
	}
}
