package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.PuzzleBox;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PuzzleBoxData extends StaticEntData
{
	private PuzzleBox puzzleBoxRef;
	public PuzzleBoxData(HashMap<String, String> puzzleBoxHM)
	{
		super(puzzleBoxHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		puzzleBoxRef = new PuzzleBox(size, new Vector2(xPos, yPos));

		//COLOR
		if (color != null)
			puzzleBoxRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		puzzleBoxRef.setTexture(tex);
		
		entData.add(puzzleBoxRef);
		ent = puzzleBoxRef;
	}
}
