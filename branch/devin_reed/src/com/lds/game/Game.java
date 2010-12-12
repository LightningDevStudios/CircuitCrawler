package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	public Level[][] GameLevels;
	public static ArrayList<Entity> entList;
	public Player player1 = new Player();
	public Player player2 = new Player();
	
	public Game ()
	{
		entList = new ArrayList<Entity>();
		entList.add(player1);
		player1.initialize(50.0f, 0.0f, 0.0f, 0.0f);
		entList.add(player2);
		player2.initialize(50.0f, 55.0f, 0.0f, 0.0f);
	}
}