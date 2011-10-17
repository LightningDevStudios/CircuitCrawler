package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.PuzzleBox;
import com.lds.math.Vector2;

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
		
		//TEXTURE
		if (textureModeEnabled)
			puzzleBoxRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			puzzleBoxRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(puzzleBoxRef);
		ent = puzzleBoxRef;
	}
}
