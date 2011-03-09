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

		//COLOR
		if (color != null)
			puzzleBoxRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			puzzleBoxRef.enableGradientMode(gradient);
		
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
