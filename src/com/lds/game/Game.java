package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.TextureLoader;
import com.lds.Enums.UIPosition;

public class Game
{
	
	//public Level[][] GameLevels;

	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	
	public TextureLoader tl;
	public EntityCleaner cleaner;
		
	//Camera data
	public static float screenW, screenH;
	public float camPosX;
	public float camPosY;
	
	public float worldMinX, worldMinY, worldMaxX, worldMaxY;
	
	//Testing data
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public UIButton btnA;
	public UIButton btnB;	public UIJoypad joypad;
	public Player player;
	public PhysBlock block;
	public Button button;
	
	//Constructors
	public Game (Context context, GL10 gl)
	{
		entList = new ArrayList<Entity>();
		UIList = new ArrayList<UIEntity>();
		
		tileset = new Tile[16][16];
		cleaner = new EntityCleaner();
		
		tl = new TextureLoader(gl, context);
		tl.load(R.drawable.tilesetcolors);
		tl.load(R.drawable.tilesetwire);
		tl.load(R.drawable.randomthings);
		tl.setTexture(1);
				
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile(Tile.TILE_SIZE_F, j, i, tileset[0].length, tileset.length);
				tileset[i][j].setTilesetMode(tl.getTexture(), 0, 0, 0, 7);
			}
		}	
		
		button = new Button(90.0f, 90.0f, RenderMode.TILESET);
		tl.setTexture(2);
		button.setTilesetMode(tl.getTexture(), 0, 0, 0, 7);
		entList.add(button);
		
		player = new Player(0.0f, 0.0f, 0.0f, RenderMode.TILESET);
		tl.setTexture(1);
		player.setTilesetMode(tl.getTexture(), 1, 0, 0, 7);
		entList.add(player);
		
		block = new PhysBlock(30.0f, 200.0f, 0.0f, RenderMode.COLOR);
		block.setColorMode(0, 255, 255, 255);
		entList.add(block);
		
		healthBar = new UIHealthBar(200.0f, 30.0f, UIPosition.TOPLEFT, Direction.RIGHT);
		healthBar.setTopPad(5.0f);
		healthBar.setLeftPad(5.0f);
		healthBar.autoPadding(5, 5, 0, 0);
				
		//						Red	  Green	Blue  Alpha
		float[] healthColor = {	0.0f, 1.0f, 0.0f, 0.9f,		//top right
								0.0f, 1.0f, 0.0f, 0.9f, 	//bottom right
								1.0f, 0.0f, 0.0f, 1.0f, 	//top left
								1.0f, 0.0f, 0.0f, 1.0f};	//bottom left
		healthBar.setGradientMode(healthColor);
		healthBar.setValue(99);
		
		
		energyBar = new UIEnergyBar(150.0f, 15.0f, UIPosition.TOPRIGHT, Direction.LEFT);
		energyBar.setTopPad(5.0f);
		energyBar.setRightPad(5.0f);
		energyBar.autoPadding(5, 0, 0, 5);
		
		float[] energyColor = {	0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 1.0f, 0.9f,
								0.0f, 0.0f, 1.0f, 0.9f };
		energyBar.setGradientMode(energyColor);
		energyBar.setValue(99);
		
		btnA = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnA.autoPadding(0.0f, 0.0f, 5.0f, 90.0f);
		//btnA.renderMode = RenderMode.COLOR;
		btnA.setColorMode(86, 93, 128, 128);
		btnA.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		//btnB.renderMode = RenderMode.COLOR;
		btnB.setColorMode(200, 93, 50, 128);
		btnB.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
		
		joypad = new UIJoypad(100, 100, UIPosition.BOTTOMLEFT);
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.setBlankMode();
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
		
		camPosX = 0.0f;
		camPosY = 0.0f;
		
		worldMinX = -500.0f;
		worldMinY = -500.0f;
		worldMaxX = 500.0f;
		worldMaxY = 500.0f;
		
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
			float entMinX = ent.xPos - (float)ent.diagonal;
			float entMaxX = ent.xPos + (float)ent.diagonal;
			float entMinY = ent.yPos - (float)ent.diagonal;
			float entMaxY = ent.yPos + (float)ent.diagonal;
			
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
