package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	//public Level[][] GameLevels;
	//Camera data
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	public float screenW, screenH, camPosX, camPosY;
	
	//Testing data
	public Player player1 = new Player();
	
	public Game (float _screenW, float _screenH)
	{
		screenW = _screenW;
		screenH = _screenH;
		player1.initialize(50.0f, 0.0f, 0.0f);
		entList.add(player1);
		camPosX = 0.0f;
		camPosY = 0.0f;
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