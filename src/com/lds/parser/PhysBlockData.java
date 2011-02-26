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
		if (rgba != null)
			physBlockRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			physBlockRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(physBlockRef);
	}
}
