package com.lds.parser;

import java.util.HashMap;
import java.util.ArrayList;

import com.lds.game.entity.PuzzleBox;
import com.lds.game.entity.Entity;

public class PuzzleBoxData extends StaticEntData
{
	private PuzzleBox puzzleBoxRef;
	public PuzzleBoxData(HashMap<String, String> puzzleBoxHM)
	{
		super(puzzleBoxHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		puzzleBoxRef = new PuzzleBox(size, xPos, yPos, circular, willCollide);
		if (rgba != null)
			puzzleBoxRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			puzzleBoxRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(puzzleBoxRef);
	}
}
