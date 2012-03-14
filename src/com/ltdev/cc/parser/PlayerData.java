package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.Player;
import com.ltdev.math.Vector2;

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

		playerRef.setTexture(tex);
		
		entData.add(playerRef);
		ent = playerRef;
	}
}
