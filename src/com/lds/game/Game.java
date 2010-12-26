package com.lds.game;

import java.util.ArrayList;

import com.lds.TilesetHelper;
import com.lds.Point;

public class Game
{
	
	//public Level[][] gameLevels;
	public ArrayList<Entity> entList;
	public Tile[][] tileset = new Tile[8][8];
	//Camera data
	public float screenW, screenH, camPosX, camPosY;
	
	//Testing data
	public Player player1 = new Player(0.0f, 100.0f, 0.0f);
	public Button button1 = new Button(0.0f, 0.0f);
	public Player player2 = new Player(0.0f, -150.0f, 0.0f);
	
	public Game (float _screenW, float _screenH)
	{
		entList = new ArrayList<Entity>();
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile((int)(Math.random() * 3), (int)(Math.random() * 3), Tile.TILE_SIZE_F, ((Tile.TILE_SIZE + 1) * j) - 100.0f, ((-Tile.TILE_SIZE - 1) * i) + 50.0f);
				
				//TODO: REMOVE THE +/- 1 BY Tile.TILE_SIZE TO GET PERFECTLY ALIGNED TILESET. 1 PX OFF FOR TESTING
				//tileset[i][j].initialize(Tile.TILE_SIZE_F, ((Tile.TILE_SIZE + 1) * j) - 100.0f, ((-Tile.TILE_SIZE - 1) * i) + 50.0f);
				System.out.print(TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7));
				if (TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7) < 10)
				{
					System.out.print(" ");
				}
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		screenW = _screenW;
		screenH = _screenH;
		entList.add(player1);
		entList.add(button1);
		entList.add(player2);
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
			ent.updateAbsolutePointLocations();
			float entMinX = 999999.9f;
			float entMaxX = 0.0f;
			float entMinY = 999999.9f;
			float entMaxY = 0.0f;
			for (Point point : ent.colPoints)
			{
				if (point.getX() > entMaxX)
				{
					entMaxX = point.getX();
				}
				else if (point.getX() < entMinX)
				{
					entMinX = point.getX();
				}
				if (point.getY() > entMaxY)
				{
					entMaxY = point.getY();
				}
				else if (point.getY() < entMinY)
				{
					entMinY = point.getY();
				}
			}
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
			{
				ent.isRendered = true;
			}
			else
			{
				ent.isRendered = false;
			}
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
	
	public void destroy (Entity ent)
	{
		entList.remove(ent);
		ent = null;
	}
}