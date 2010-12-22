package com.lds.game;

import java.util.ArrayList;

public class Game
{
	
	//public Level[][] GameLevels;
	//update - made entList static, so each new entity can be added to the list when initialized - Devin
	public ArrayList<Entity> entList;
	public Tile[][] tileset = new Tile[10][10];
	
	//Camera data
	public float screenW, screenH, camPosX, camPosY;
	
	//Testing data
	public Player player1 = new Player();
	public Player player2 = new Player();
	public Player player3 = new Player();
	public Tile tile1 = new Tile(3,2);
	
	public Game (float _screenW, float _screenH)
	{
		entList = new ArrayList<Entity>();
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				//tileset[i][j] = new Tile((int)Math.random() * 7, (int)Math.random() * 7);
				tileset[i][j] = new Tile(3,4);
				tileset[i][j].initialize(32.0f, (33 * j) - 300.0f, (33 * i) + 50.0f);
			}
		}
		screenW = _screenW;
		screenH = _screenH;
		tile1.initialize(256.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f);
		entList.add(tile1);
		
		//entList.add(player1);
		//System.out.println(com.lds.TilesetHelper.getTilesetIndex(player1.texture, 0, 7));
		//player2.initialize(25.0f, 30.0f, 30.0f, 0.0f, 1.0f, 1.0f);
		//entList.add(player2);
		//player3.initialize(30.0f, 174.0f, 160.0f);
		camPosX = 0.0f;
		camPosY = 0.0f;
		
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		player2.move(30.0f, 50.0f);
		updateLocalEntities();
		/*if (player1.isColliding(player2))
		{
			player3.initialize(30.0f, 0.0f, -100.0f);
			entList.add(player3);
			updateLocalEntities();
		}*/
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
			float entMinX = ent.xPos - (ent.size * (float)Math.sqrt(2) / 2);
			float entMaxX = ent.xPos + (ent.size * (float)Math.sqrt(2) / 2);
			float entMinY = ent.yPos - (ent.size * (float)Math.sqrt(2) / 2);
			float entMaxY = ent.yPos + (ent.size * (float)Math.sqrt(2) / 2);
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
			{
				ent.isRendered = true;
			}
			else
				ent.isRendered = false;
		}
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				float entMinX = tileset[i][j].xPos - (tileset[i][j].size * (float)Math.sqrt(2) / 2);
				float entMaxX = tileset[i][j].xPos + (tileset[i][j].size * (float)Math.sqrt(2) / 2);
				float entMinY = tileset[i][j].yPos - (tileset[i][j].size * (float)Math.sqrt(2) / 2);
				float entMaxY = tileset[i][j].yPos + (tileset[i][j].size * (float)Math.sqrt(2) / 2);
				
				//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
				if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
				{
					tileset[i][j].isRendered = true;
				}
				else
					tileset[i][j].isRendered = false;
			}
		}
	}
	
	public void updateLocal(Entity ent)
	{
		float minX, maxX, minY, maxY;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
		float entMinX = ent.xPos - (ent.size * (float)Math.sqrt(2) / 2);
		float entMaxX = ent.xPos + (ent.size * (float)Math.sqrt(2) / 2);
		float entMinY = ent.yPos - (ent.size * (float)Math.sqrt(2) / 2);
		float entMaxY = ent.yPos + (ent.size * (float)Math.sqrt(2) / 2);
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
			{
				ent.isRendered = true;
			}
			else
				ent.isRendered = false;
	}
	
	public void updateLocalTileset()
	{
		
	}
	
	//does some moving, rotating, scaling and collion check for testing
	public void testMove ()
	{
		//TODO: Fill out this class
	}
}