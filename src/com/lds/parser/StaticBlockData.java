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
		if (rgba != null)
			staticBlockRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			staticBlockRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(staticBlockRef);
	}
}
