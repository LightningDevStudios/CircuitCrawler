package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Player;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData extends CharacterData
{
	private Player playerRef;
	
	public PlayerData(HashMap<String, String> playerHM)
	{
		super(playerHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		playerRef = new Player(new Vector2(xPos, yPos), angle);

		//COLOR
		if (color != null)
			playerRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			playerRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			playerRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			playerRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(playerRef);
		ent = playerRef;
	}
}
