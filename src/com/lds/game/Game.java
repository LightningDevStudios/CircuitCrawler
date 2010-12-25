package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.TextureLoader;
import com.lds.TilesetHelper;

public class Game
{
	
	//public Level[][] GameLevels;
	
	public ArrayList<Entity> entList;
	public Tile[][] tileset = new Tile[8][8];
	public ArrayList<Entity> UIList;
	
	public TextureLoader tl;
	
	//Camera data
	public float screenW, screenH, camPosX, camPosY;
	
	//Testing data
	public Player player1 = new Player();
	public Button button1 = new Button();
	public Player player2 = new Player();
	public Tile tile1 = new Tile(3,2);
	
	public Game (float _screenW, float _screenH)
	{
		entList = new ArrayList<Entity>();
		
		screenW = _screenW;
		screenH = _screenH;
		player1.initialize(30.0f, 0.0f, 100.0f);
		//entList.add(player1);
		button1.initialize(40.0f, 0.0f, 0.0f);
		//entList.add(button1);
		player2.initialize(20.0f, 0.0f, -150.0f);
		//entList.add(player2);
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
	
	public void initializeTileset()
	{
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile((int)(Math.random() * 8), (int)(Math.random() * 8));
				
				//REMOVE THE +/- 1 BY Tile.TILE_SIZE TO GET PERFECTLY ALIGNED TILESET. 1 PX OFF FOR TESTING
				tileset[i][j].initialize(Tile.TILE_SIZE_F, ((Tile.TILE_SIZE + 1) * j) - 100.0f, ((-Tile.TILE_SIZE - 1) * i) + 50.0f);
				if (j % 2 == 0)
				{
					tl.setTexture(0);
				}
				else
				{
					tl.setTexture(1);
				}
				tileset[i][j].setTexture(tl.getTexture());
				
				System.out.print(TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7));
				if (TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7) < 10)
				{
					System.out.print(" ");
				}
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		
		updateLocalTileset();
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
			{
				ent.isRendered = false;
			}
		}
	}
	
	public void updateLocalTileset()
	{
		float minX, maxX, minY, maxY;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
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
}