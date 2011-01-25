package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.StringRenderer;
import com.lds.Texture;
import com.lds.TextureLoader;
import com.lds.Enums.UIPosition;

import com.lds.UI.*;
import com.lds.game.entity.Button;
import com.lds.game.entity.Door;
import com.lds.game.entity.Entity;
import com.lds.game.entity.PhysBlock;
import com.lds.game.entity.Player;
import com.lds.game.entity.Tile;
import com.lds.trigger.*;

public class Game
{
	
	//public Level[][] GameLevels;

	public static boolean worldOutdated;
	
	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	public ArrayList<Trigger> triggerList;
	
	public EntityCleaner cleaner;
		
	//Camera data
	public static float screenW, screenH;
	public float camPosX;
	public float camPosY;
	
	public float worldMinX, worldMinY, worldMaxX, worldMaxY;
	
	//Texture data
	public static Texture tilesetcolors;
	public static Texture tilesetwire;
	public static Texture randomthings;
	public static Texture text;
	public Texture someText;
	
	
	//Testing data
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public UIButton btnA;
	public UIButton btnB;	
	public UIJoypad joypad;
	public UITextBox textbox;
	public Player player;
	public PhysBlock block;
	public Button button;
	public Door door;
	
	//Constructors
	public Game (Context context, GL10 gl)
	{
		tilesetcolors = new Texture(R.drawable.tilesetcolors, 128, 128, 8, 8, context);
		tilesetwire = new Texture(R.drawable.tilesetwire, 128, 128, 8, 8, context);
		randomthings = new Texture(R.drawable.randomthings, 256, 256, 8, 8, context);
		text = new Texture(R.drawable.text, 256, 256, 16, 8, context);
		
		entList = new ArrayList<Entity>();
		UIList = new ArrayList<UIEntity>();
		triggerList = new ArrayList<Trigger>();
		
		tileset = new Tile[16][16];
		cleaner = new EntityCleaner();
		StringRenderer sr = StringRenderer.getInstance();
		TextureLoader.getInstance().initialize(gl);
		
		sr.loadTextTileset(text);
		
		someText = new Texture("Testing!", sr);
		
		TextureLoader tl = TextureLoader.getInstance();
		
		tl.loadTexture(tilesetcolors);
		tl.loadTexture(tilesetwire);
		tl.loadTexture(randomthings);
		tl.loadTexture(someText);
						
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile(Tile.TILE_SIZE_F, j, i, tileset[0].length - 1, tileset.length - 1);
				tileset[i][j].setTexture(tilesetwire);
				if (i == 0 || j == 0 || i == tileset.length - 1 || j == tileset[0].length - 1 || (i < 4 && (j < 4 || j > 8)) || (i > 10 && (j < 4 || j > 8)))
				{
					tileset[i][j].setAsWall();
				}
				else if (i == 4 || i == 5 || (i == 6 || i == 9 || i == 10) && (j < 4 || j > 8) || (i == 7 || i == 8) && (j < 3 || j > 9))
				{
					tileset[i][j].setAsPit();
				}
				/*else if (i == 3)
				{
					tileset[i][j].setAsBridge();
				}*/
				else
				{
					tileset[i][j].setAsFloor();
				}
			}
		}	
		
		door = new Door (100.0f, 300.0f, RenderMode.COLOR);
		door.setColorMode(255, 225, 0, 225);
		entList.add(door);
		door.setWillCollideWithPlayer(true);
		
		button = new Button(90.0f, 90.0f, RenderMode.TILESET, door);
		button.setTilesetMode(randomthings, 0, 0);
		entList.add(button);
		button.setWillCollideWithPlayer(false);
		
		block = new PhysBlock(50.0f, -100.0f, -100.0f, RenderMode.COLOR);
		block.setColorMode(255, 255, 0, 255);
		entList.add(block);
		block.setWillCollideWithPlayer(true);
		block.scale(2.0f, 2.0f);
		
		player = new Player(0.0f, 0.0f, 0.0f, RenderMode.TILESET);
		player.setTilesetMode(tilesetwire, 1, 0);
		entList.add(player);
		player.setWillCollideWithPlayer(false);
		player.enableUserControl();
		
		triggerList.add(new Trigger(new CauseButton(button), new EffectDoor(door)));
		triggerList.add(new Trigger(new CauseDoneScaling(player), new EffectRemoveEntity(player)));
		
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
		btnA.setColorMode(86, 93, 128, 128);
		btnA.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.setColorMode(200, 93, 50, 128);
		btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		joypad = new UIJoypad(100, 100, UIPosition.BOTTOMLEFT);
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.setBlankMode();
		
		textbox = new UITextBox(112, 32, UIPosition.TOPLEFT);
		textbox.autoPadding(5.0f, 5.0f, 0.0f, 0.0f);
		textbox.setTextureMode(someText);
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
		UIList.add(textbox);
		
		camPosX = 0.0f;
		camPosY = 0.0f;
		
		worldMinX = (-Tile.TILE_SIZE_F * (tileset[0].length / 2)) + (screenW / 2);
		worldMinY = (-Tile.TILE_SIZE_F * (tileset.length / 2)) + (screenH / 2);
		worldMaxX = (Tile.TILE_SIZE_F * (tileset[0].length / 2)) - (screenW / 2);
		worldMaxY = (Tile.TILE_SIZE_F * (tileset.length / 2)) - (screenH / 2);
		
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
			float entMinX = ent.getXPos() - (float)ent.getDiagonal();
			float entMaxX = ent.getXPos() + (float)ent.getDiagonal();
			float entMinY = ent.getYPos() - (float)ent.getDiagonal();
			float entMaxY = ent.getYPos() + (float)ent.getDiagonal();
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
			{
				ent.setRendered(true);
			}
			else
			{
				ent.setRendered(false);
			}
		}
	}
	
	public void updateLocalTileset()
	{
		float minX, maxX, minY, maxY, tilesetHalfWidth, tilesetHalfHeight;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
		
		tilesetHalfWidth = tileset[0].length * Tile.TILE_SIZE_F / 2;
		tilesetHalfHeight = tileset.length * Tile.TILE_SIZE_F / 2;
		
		int minXBound = (int)(minX + tilesetHalfWidth) / Tile.TILE_SIZE;
		int maxXBound = ((int)(Math.ceil(maxX + tilesetHalfWidth) - 1) / Tile.TILE_SIZE);
		int minYBound = ((int)(Math.abs(maxY - tilesetHalfHeight) - 1) / Tile.TILE_SIZE);
		int maxYBound = ((int)(Math.ceil(Math.abs(minY - tilesetHalfHeight)) - 1) / Tile.TILE_SIZE);

		//set all to false
		for (Tile[] ts : tileset)
		{
			for(Tile t : ts)
			{
				t.setRendered(false);
			}
		}
		
		//only set values in bounds to true
		for (int i = minYBound; i <= maxYBound; i++)
		{
			for (int j = minXBound; j <= maxXBound; j++)
			{
					tileset[i][j].setRendered(true);
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
		float entMinX = ent.getXPos() - (float)ent.getDiagonal();
		float entMaxX = ent.getXPos() + (float)ent.getDiagonal();
		float entMinY = ent.getYPos() - (float)ent.getDiagonal();
		float entMaxY = ent.getYPos() + (float)ent.getDiagonal();
			
		//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
		if (entMinX <= maxX && entMaxX >= minX && entMinY <= maxY && entMaxY >= minY)
		{
			ent.setRendered(true);
		}
		else
		{
			ent.setRendered(false);
		}
	}
	
	public Tile nearestTile(Entity ent)
	{	
		float tilesetHalfWidth = tileset[0].length * Tile.TILE_SIZE_F / 2;
		float tilesetHalfHeight = tileset.length * Tile.TILE_SIZE_F / 2;
		
		int x = (int)(ent.getXPos() + tilesetHalfWidth) / Tile.TILE_SIZE;
		int y = (int)(Math.abs(ent.getYPos() - tilesetHalfHeight)) / Tile.TILE_SIZE;
		
		if (x < tileset[0].length && x >= 0 && y < tileset.length && y >= 0)
		{
			return tileset[y][x];
		}
		
		return null;
		
	}
}
