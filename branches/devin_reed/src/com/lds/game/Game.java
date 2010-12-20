package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	//public Level[][] GameLevels;
	//update - made entList static, so each new entity can be added to the list when initialized - Devin
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	public float screenW, screenH, camPosX, camPosY;
	//Testing data
	public Player player1 = new Player();
	public Player player2 = new Player();
	public Player player3 = new Player();
	public Player player4 = new Player();
	
	public Game (float _screenW, float _screenH)
	{
		screenW = _screenW;
		screenH = _screenH;
		player1.initialize(50.0f, 0.0f, -100.0f, 30.0f, 1.0f, 2.0f, false, true);
		entList.add(player1);
		player3.initialize(10.0f, -40.0f, -100.0f);
		entList.add(player3);
		//player4.initialize(20.0f, -69.0f, 106.4f);
		//entList.add(player4);
		camPosX = 0.0f;
		camPosY = 0.0f;
		//call this every time the player moves.
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		updateLocalEntities();
		//player1.rotate(30.0f);
		player1.move(100.0f, 69.0f);
		player3.moveTo(-60.0f, 43.8f);
		player1.rotateTo(60.0f);
		player3.rotate(-45.0f);
		player1.scaleTo(0.2f, 4.8f);
		player3.scale(3.4f,0.8f);
	}
	
	public void updateLocalEntities()
	{
		//define current screen bounds
		float minX, maxX, minY, maxY;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
		
		for(Entity ent : entList)
		{
			//define max square bounds
			//TODO factor in scaling / rotation for calculating rendered items
			float entMinX = ent.xPos - (ent.size / 2);
			float entMaxX = ent.xPos + (ent.size / 2);
			float entMinY = ent.yPos - (ent.size / 2);
			float entMaxY = ent.yPos + (ent.size / 2);
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
			{
				ent.isRendered = true;
			}
			else
				ent.isRendered = false;
		}
	}
}