package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.TextureLoader;
import com.lds.TilesetHelper;
import com.lds.Point;
import com.lds.Enums.UIPosition;

public class Game
{
	
	//public Level[][] GameLevels;

	public ArrayList<Entity> entList;
	public Tile[][] tileset = new Tile[8][8];
	public ArrayList<UIEntity> UIList;
	
	public TextureLoader tl;
	public EntityCleaner cleaner;
	
	//Camera data
	public float screenW;
	public float screenH;
	public float camPosX;
	public float camPosY;
	
	//Testing data
	public Player player1 = new Player(-100.0f, 50.0f, 0.0f);
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public Sprite spr1;
	public Sprite spr2;

	
	//Constructors
	public Game (float _screenW, float _screenH, Context context, GL10 gl)
	{
		entList = new ArrayList<Entity>();
		UIList = new ArrayList<UIEntity>();
		
		tl = new TextureLoader(gl, context);
		tl.load(R.drawable.tilesetcolors);
		tl.load(R.drawable.tilesetwire);
		
		tl.setTexture(1);
		
		cleaner = new EntityCleaner();
		
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				//TODO: REMOVE THE +/- 1 BY Tile.TILE_SIZE TO GET PERFECTLY ALIGNED TILESET. 1 PX OFF FOR TESTING
				tileset[i][j] = new Tile((int)(Math.random() * 8), (int)(Math.random() * 8), Tile.TILE_SIZE_F, ((Tile.TILE_SIZE + 1) * j) - 100.0f, ((-Tile.TILE_SIZE - 1) * i) + 50.0f);
				tileset[i][j].setTexture(tl.getTexture());
				
				System.out.print(TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7));
				
				if (TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7) < 10)
					System.out.print(" ");
				
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		
		
		
		screenW = _screenW;
		screenH = _screenH;

		//entList.add(player1);
		//entList.add(button1);
		//entList.add(player2);
		spr1 = new Sprite(7,7, 96.0f, 250.0f, 350.0f, 0.0f, 1.0f, 1.0f, PhysEnt.DEFAULT_SPEED);
		spr2 = new Sprite(7,7, 64.0f, -300.0f, -300.0f, 73.6f, 1.0f, 1.0f, PhysEnt.DEFAULT_SPEED);
		tl.setTexture(0);
		
		spr1.setTexture(tl.getTexture());
		spr2.setTexture(tl.getTexture());
		
		spr1.moveTo(-200.0f, -100.0f);
		spr1.rotateTo(45.0f);
		
		spr2.moveTo(150.0f, 150.0f);
		spr2.rotateTo(200.3f);
		
		entList.add(spr1);
		entList.add(spr2);
		
		healthBar = new UIHealthBar(200.0f, 30.0f, UIPosition.TOPLEFT, Direction.RIGHT);
		healthBar.originalTopPad = 5.0f;
		healthBar.originalLeftPad = 5.0f;
		healthBar.autoPadding(5, 5, 0, 0);
		healthBar.renderMode = RenderMode.GRADIENT;
		
		//						Red	  Green	Blue  Alpha
		float[] healthColor = {	0.0f, 1.0f, 0.0f, 0.9f,		//top right
								0.0f, 1.0f, 0.0f, 0.9f, 	//bottom right
								1.0f, 0.0f, 0.0f, 1.0f, 	//top left
								1.0f, 0.0f, 0.0f, 1.0f};	//bottom left
		healthBar.setGradient(healthColor);
		healthBar.updatePosition(screenW, screenH);
		
		
		energyBar = new UIEnergyBar(150.0f, 15.0f, UIPosition.TOPRIGHT, Direction.LEFT);
		energyBar.originalTopPad = 5.0f;
		energyBar.originalRightPad = 5.0f;
		energyBar.autoPadding(5, 0, 0, 5);
		energyBar.renderMode = RenderMode.GRADIENT;
		
		float[] energyColor = {	0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 1.0f, 0.9f,
								0.0f, 0.0f, 1.0f, 0.9f };
		energyBar.setGradient(energyColor);
		energyBar.updatePosition(screenW, screenH);
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		camPosX = 0.0f;
		camPosY = 0.0f;
		
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		updateLocalEntities();
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
				float entMinX = tileset[i][j].xPos - (tileset[i][j].size);
				float entMaxX = tileset[i][j].xPos + (tileset[i][j].size);
				float entMinY = tileset[i][j].yPos - (tileset[i][j].size);
				float entMaxY = tileset[i][j].yPos + (tileset[i][j].size);
								
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
			{
				ent.isRendered = false;
			}
	}
	
}
