package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Spike;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class SpikeData extends StaticEntData
{
	private Spike spikeRef;
	public SpikeData(HashMap<String, String> sdHM)
	{
		super(sdHM);
	}

	public void createInst(ArrayList<Entity> entData)
	{
		spikeRef = new Spike(size, new Vector2(xPos, yPos), angle);
		
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
