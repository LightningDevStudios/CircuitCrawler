package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	public Level[][] GameLevels;
	public ArrayList<Entity> entList;
	public Player player1 = new Player();
	public Player player2 = new Player();
	
	public Game ()
	{
		entList = new ArrayList<Entity>();
		player1.initialize(20.0f, 10.0f, 20.0f, 0.0f, 1.0f, 1.0f);
		entList.add(player1);
		player2.initialize(20.0f, 60.0f, 70.0f, 0.0f, 1.0f, 1.0f);
		entList.add(player2);
	}
}