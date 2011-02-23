package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Player;

public class PlayerData extends CharacterData
{
	Player playerRef;
	
	public PlayerData(HashMap<String, String> playerHM)
	{
		super(playerHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		playerRef = new Player(xPos, yPos, angle);
		if (rgba != null)
			playerRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			playerRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(playerRef);
	}
}