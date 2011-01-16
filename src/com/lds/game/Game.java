package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.TextRenderer;
import com.lds.Texture;
import com.lds.TextureLoader;
import com.lds.Enums.UIPosition;

public class Game
{
	
	//public Level[][] GameLevels;

	public static boolean worldOutdated;
	
	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	
	public TextureLoader tl;
	public EntityCleaner cleaner;
	public TextRenderer tr;
		
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
	
	
	//Testing data
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public UIButton btnA;
	public UIButton btnB;	
	public UIJoypad joypad;
	public UIImage image;
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
		
		tileset = new Tile[16][16];
		cleaner = new EntityCleaner();
		tr = new TextRenderer(text);
		tl = new TextureLoader(gl);
		
		Texture someText = tr.textToTexture("($)&(+)");
		
		tl.loadTexture(tilesetcolors);
		tl.loadTexture(tilesetwire);
		tl.loadTexture(randomthings);
		tl.loadTexture(someText);
						
		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile(Tile.TILE_SIZE_F, j, i, tileset[0].length - 1, tileset.length - 1);
				if (i == 0 || j == 0 || i == tileset.length - 1 || j == tileset.length - 1)
				{
					tileset[i][j].setTilesetMode(tilesetwire, 2, 0);
					tileset[i][j].setAsWall();
				}
				else
				{
					tileset[i][j].setTilesetMode(tilesetwire, 0, 0);
					tileset[i][j].setAsFloor();
				}
			}
		}	
				
		door = new Door (-100.0f, -100.0f, RenderMode.COLOR);
		door.setColorMode(255, 0, 0, 100.0f);
		entList.add(door);
		door.setWillCollideWithPlayer(true);
		
		button = new Button(90.0f, 90.0f, RenderMode.TILESET, door);
		button.setTilesetMode(randomthings, 0, 0);
		entList.add(button);
		button.setWillCollideWithPlayer(false);
		
		block = new PhysBlock(30.0f, 200.0f, 0.0f, RenderMode.COLOR);
		block.setColorMode(0, 255, 255, 255);
		entList.add(block);
		block.setWillCollideWithPlayer(true);
		
		player = new Player(0.0f, 0.0f, 0.0f, RenderMode.TILESET);
		player.setTilesetMode(tilesetwire, 1, 0);
		entList.add(player);
		player.setWillCollideWithPlayer(false);
		
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
		
		image = new UIImage(112, 32, UIPosition.TOPLEFT);
		image.autoPadding(5.0f, 5.0f, 0.0f, 0.0f);
		image.setTextureMode(someText);
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
		UIList.add(image);
		
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
			float entMinX = ent.xPos - (float)ent.getDiagonal();
			float entMaxX = ent.xPos + (float)ent.getDiagonal();
			float entMinY = ent.yPos - (float)ent.getDiagonal();
			float entMaxY = ent.yPos + (float)ent.getDiagonal();
			
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
		float minX, maxX, minY, maxY, tilesetWidth, tilesetHeight;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
		
		tilesetWidth = tileset[0].length * Tile.TILE_SIZE_F;
		tilesetHeight = tileset.length * Tile.TILE_SIZE_F;
		
		int minXBound = (int)Math.floor(((minX + (tilesetWidth / 2)) / Tile.TILE_SIZE_F) - 1);
		int maxXBound = (int)Math.ceil(((maxX + (tilesetWidth / 2)) / Tile.TILE_SIZE_F) - 1);
		int minYBound = 14 - (int)Math.floor(((maxY + (tilesetHeight / 2)) / Tile.TILE_SIZE_F) - 1);
		int maxYBound = 15 - (int)Math.ceil(((minY + (tilesetHeight / 2)) / Tile.TILE_SIZE_F) - 1);
		
		if (minXBound < 0) { minXBound = 0; }
		if (maxXBound > tileset[0].length) { maxXBound = tileset[0].length; }
		
		if (minYBound < 0) { minYBound = 0; }
		if (maxYBound > tileset.length) { maxYBound = tileset.length; }
		
		//set all to false
		for (Tile[] ts : tileset)
		{
			for(Tile t : ts)
			{
				t.isRendered = false;
			}
		}
		
		//only set values in bounds to true
		for (int i = minYBound; i <= maxYBound; i++)
		{
			for (int j = minXBound; j <= maxXBound; j++)
			{
					tileset[i][j].isRendered = true;
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
		float entMinX = ent.xPos - (float)ent.getDiagonal();
		float entMaxX = ent.xPos + (float)ent.getDiagonal();
		float entMinY = ent.yPos - (float)ent.getDiagonal();
		float entMaxY = ent.yPos + (float)ent.getDiagonal();
			
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
	
	public Tile nearestTile(Entity ent)
	{
		int x = (int)ent.getXPos();
		int y = (int)ent.getYPos();
		
		int tileLeft = x / Tile.TILE_SIZE;
		int tileRight = tileLeft + 1;
		int tileDown = y / Tile.TILE_SIZE;
		int tileUp = tileDown + 1;
		
		int left = x - (Tile.TILE_SIZE * (x / Tile.TILE_SIZE));
		int right = Tile.TILE_SIZE - left;
		int down = y - (Tile.TILE_SIZE) * (y / Tile.TILE_SIZE);
		int up = Tile.TILE_SIZE - down;
		
		if (left <= right && up <= down)
		{
			return tileset[tileUp][tileLeft];
		}
		else if (left <= right && up > down)
		{
			return tileset[tileDown][tileLeft];
		}
		else if (left > right && up <= down)
		{
			return tileset[tileUp][tileRight];
		}
		else if (left > right && up > down)
		{
			return tileset[tileDown][tileRight];
		}
		else
		{
			return null;
		}
	}
}
