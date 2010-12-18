package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	//public Level[][] GameLevels;
	//update - made entList static, so each new entity can be added to the list when initialized - Devin
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	
	//Camera data
	public float screenW, screenH, camPosX, camPosY;
	
	//Testing data
	public Player player1 = new Player();
	public Player player2 = new Player();
	public Player player3 = new Player();
	
	public Game (float _screenW, float _screenH)
	{
		screenW = _screenW;
		screenH = _screenH;
		player1.initialize(23.0f, -43.0f, -58.0f, 32.0f, 10.5f, 2.1f);
		entList.add(player1);
		player2.initialize(46.0f, 46.0f, 38.9f, -65.0f, 0.2f, 6.0f);
		entList.add(player2);
		//player3.initialize(30.0f, 174.0f, 160.0f);
		camPosX = 0.0f;
		camPosY = 0.0f;
		
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		player2.move(30.0f, 50.0f);
		updateLocalEntities();
		if (player1.isColliding(player2))
		{
			player3.initialize(30.0f, 0.0f, -100.0f);
			entList.add(player3);
			updateLocalEntities();
		}
		//does some moving, rotating, scaling and collion check for testing - Devin
		testMove();
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
	
	//does some moving, rotating, scaling and collion check for testing
	public void testMove ()
	{
		//TODO: Fill out this class
	}
}