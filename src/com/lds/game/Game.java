package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.TextureLoader;
import com.lds.TilesetHelper;
import com.lds.Point;
import com.lds.Enums.UIPosition;

public class Game
{
	
	//public Level[][] GameLevels;

	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	
	public TextureLoader tl;
	public EntityCleaner cleaner;
	
	//Timer data
	public long timeMs;
	public int timeS;
	public int timeM;
	
	//Camera data
	public float screenW;
	public float screenH;
	public float camPosX;
	public float camPosY;
	
	//Testing data
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public UIButton btnA;
	public UIButton btnB;
	public UIJoypad joypad;
	public Player player;
	public PhysBlock block;
	
	//Constructors
	public Game (float _screenW, float _screenH, Context context, GL10 gl)
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
				tileset[i][j] = new Tile(0, 0, Tile.TILE_SIZE_F, (Tile.TILE_SIZE * j) - 100.0f, (Tile.TILE_SIZE * i) + 50.0f);
				tileset[i][j].setTexture(tl.getTexture());
				
				System.out.print(TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7));
				
				if (TilesetHelper.getTilesetIndex(tileset[i][j].texture, 0, 7) < 10)
					System.out.print(" ");
				
				System.out.print("\t");
			}
			System.out.print("\n");
		}		player = new Player(0.0f, 0.0f, 90.0f);
		tl.setTexture(1);
		player.setTexture(tl.getTexture());
		player.setTilesetCoords(1, 0);
		player.renderMode = RenderMode.TILESET;
		entList.add(player);
		
		block = new PhysBlock(200.0f, 0.0f);
		block.renderMode = RenderMode.COLOR;
		block.setColor(0, 255, 255, 255);
		entList.add(block);
		
		screenW = _screenW;
		screenH = _screenH;

		
		
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
		
		btnA = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnA.autoPadding(0.0f, 0.0f, 5.0f, 90.0f);
		btnA.renderMode = RenderMode.COLOR;
		btnA.setColor(86, 93, 128, 128);
		btnA.updatePosition(screenW, screenH);
		btnA.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.renderMode = RenderMode.COLOR;
		btnB.setColor(200, 93, 50, 128);
		btnB.updatePosition(screenW, screenH);
		btnB.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
		
		joypad = new UIJoypad(100, 100, UIPosition.BOTTOMLEFT);
		joypad.autoPadding(5.0f, 0.0f, 5.0f, 0.0f);
		joypad.renderMode = RenderMode.BLANK;
		joypad.updatePosition(screenW, screenH);
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
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
	
	public void setHeldObjectPosition (HoldObject heldEnt)
	{
		heldEnt.hold();
		float heldDistance = player.halfSize + heldEnt.halfSize + 10.0f;
		player.initializeCollisionVariables();
		player.holdObject();
		heldEnt.setPos((float)Math.cos(player.rad) * heldDistance + player.xPos, (float)Math.sin(player.rad) * heldDistance + player.yPos);
		heldEnt.setAngle(player.angle);
		System.out.println("FAP");
	}
	
	public void updateHeldObjectPosition (HoldObject heldEnt)
	{
		float heldDistance = player.halfSize + heldEnt.halfSize + 10.0f;
		player.initializeCollisionVariables();
		heldEnt.setPos((float)Math.cos(player.rad) * heldDistance + player.xPos, (float)Math.sin(player.rad) * heldDistance + player.yPos);
		heldEnt.setAngle(player.angle);
	}	
}
