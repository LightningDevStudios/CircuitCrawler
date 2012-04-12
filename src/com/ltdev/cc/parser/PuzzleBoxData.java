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
		
		puzzleBoxRef.setTexture(tex);
		
		entData.add(puzzleBoxRef);
		ent = puzzleBoxRef;
	}
}
