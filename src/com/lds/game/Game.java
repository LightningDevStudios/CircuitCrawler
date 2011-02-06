package com.lds.game;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.lds.Animation;
import com.lds.EntityManager;
import com.lds.Enums.Direction;
import com.lds.Stopwatch;
import com.lds.StringRenderer;
import com.lds.Texture;
import com.lds.TextureLoader;
import com.lds.Vector2f;
import com.lds.Enums.UIPosition;
import com.lds.Enums.EnemyType;


import com.lds.UI.*;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.trigger.*;

import com.lds.parser.Parser;

public class Game
{
	
	//public Level[][] GameLevels;

	public static boolean worldOutdated;
	
	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	public ArrayList<Trigger> triggerList;
	public EntityManager cleaner;
		
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
	public PhysCircle circle;
	public Button button;
	public Door door;
	public Sprite spr;
	public Blob blob1, blob2;
	
	public Animation spriteAnim;
	
	//Events
	public OnGameOverListener gameOverListener;
	public static OnPuzzleActivatedListener puzzleActivatedListener;
	
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
		cleaner = new EntityManager();
		StringRenderer sr = StringRenderer.getInstance();
		TextureLoader.getInstance().initialize(gl);
		
		sr.loadTextTileset(text);
		
		someText = new Texture("Testing!", sr);
		
		SoundPlayer.getInstance().initialize(context);
		
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
				tileset[i][j].enableTilesetMode(tilesetwire, 0, 0);
				if (i == 0 || j == 0 || i == tileset.length - 1 || j == tileset[0].length - 1 || (i < 4 && (j < 4 || j > 8)) || (i == 10 && j != 6) || (i > 10 && (j < 4 || j > 8)))
				{
					tileset[i][j].setAsWall();
				}
				else if (i == 4 || i == 5 || (i == 6 || i == 9 || i == 10) && (j < 4 || j > 8) || (i == 7 || i == 8) && (j < 3 || j > 9))
				{
					tileset[i][j].setAsPit();
				}
				else
				{
					tileset[i][j].setAsFloor();
				}
			}
		}	
		
		door = new Door (-108.0f, -180.0f);
		door.enableTilesetMode(tilesetwire, 0, 2);
		entList.add(door);
		
		button = new Button(36.0f, -320.0f);
		button.enableTilesetMode(randomthings, 0, 0);
		entList.add(button);
		
		block = new PhysBlock(Entity.DEFAULT_SIZE, -215.0f, -350.0f);
		block.enableTilesetMode(tilesetwire, 2, 1);
		//block.enableColorMode(1.0f, 1.0f, 1.0f, 0.5f);
		float[] initGM = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
		float[] interpGM = {0.1f, 0.1f, 0.1f, 1.0f, 0.3f, 0.8f, 0.9f, 1.0f, 0.1f, 0.8f, 0.1f, 1.0f, 0.9f, 0.2f, 0.1f, 1.0f};
		block.enableGradientMode(initGM);
		block.setColorInterpSpeed(0.01f);
		//block.initColorInterp(0.5f, 0.7f, 0.3f, 0.5f);
		block.initGradientInterp(interpGM);
		entList.add(block);
		
		blob1 = new Blob(-150.0f, -350.0f, EnemyType.STALKER);
		blob1.enableTilesetMode(tilesetcolors, 2, 1);
		entList.add(blob1);
		
		blob2 = new Blob(0.0f, 0.0f, EnemyType.TURRET);
		blob2.enableTilesetMode(tilesetcolors, 2, 1);
		entList.add(blob2);
				
		Button button1 = new Button(108.0f, 0.0f);
		button1.enableTilesetMode(randomthings, 0, 0);
		entList.add(button1);
				
		Button button2 = new Button(-324.0f, 0.0f);
		button2.enableTilesetMode(randomthings, 0, 0);
		entList.add(button2);
		

		PhysBlock block1 = new PhysBlock(50, 0, 108);
		block1.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(block1);
		 
		PhysBlock block2 = new PhysBlock(50, -216, 108);
		block2.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(block2);
		
		spriteAnim = new Animation(tilesetwire, 0, 7, 7, 0, 3000);
		spr = new Sprite(50, -100, 100, 45, 1, 1, spriteAnim);
		spr.enableTextureMode(tilesetwire);
		entList.add(spr);
		
		//TODO NOPE LOL
		/*circle = new PhysCircle(50.0f, -100.0f, -310.0f);
		circle.setTilesetMode(tilesetwire, 1, 2);
		entList.add(circle);
		circle.setWillCollideWithPlayer(true);
		*/
		
		player = new Player(-108.0f, -450.0f, 0.0f);
		player.enableTilesetMode(tilesetwire, 1, 0);
		entList.add(player);
		player.enableUserControl();
				
		//spr = new Sprite(30.0f, -108.0f, -300.0f, 45.0f, 1.0f, 1.0f, 10, 90, 1, spriteAnim);
		//entList.add(spr);
		
		CauseAND bridgeAND = new CauseAND(new CauseButton(button1), new CauseButton(button2));
		
		triggerList.add(new Trigger(new CauseButton(button), new EffectDoor(door)));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][6])));
		//triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][7])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][6])));
		//triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][7])));
		
		healthBar = new UIHealthBar(200.0f, 30.0f, UIPosition.TOPLEFT, Direction.RIGHT, player);
		healthBar.setTopPad(5.0f);
		healthBar.setLeftPad(5.0f);
		healthBar.autoPadding(5, 5, 0, 0);
				
		//						Red	  Green	Blue  Alpha
		float[] healthColor = {	0.0f, 1.0f, 0.0f, 0.9f,		//top right
								0.0f, 1.0f, 0.0f, 0.9f, 	//bottom right
								1.0f, 0.0f, 0.0f, 1.0f, 	//top left
								1.0f, 0.0f, 0.0f, 1.0f};	//bottom left
		healthBar.enableGradientMode(healthColor);
		healthBar.setValue(99);
		
		energyBar = new UIEnergyBar(150.0f, 15.0f, UIPosition.TOPRIGHT, Direction.LEFT, player);
		energyBar.setTopPad(5.0f);
		energyBar.setRightPad(5.0f);
		energyBar.autoPadding(5, 0, 0, 5);
		
		float[] energyColor = {	0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 0.3f, 1.0f,
								0.0f, 0.0f, 1.0f, 0.9f,
								0.0f, 0.0f, 1.0f, 0.9f };
		energyBar.enableGradientMode(energyColor);
		energyBar.setValue(99);
		
		btnA = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnA.autoPadding(0.0f, 0.0f, 5.0f, 90.0f);
		btnA.enableColorMode(65, 200, 65, 128);
		btnA.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.enableColorMode(200, 65, 65, 100);
		btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		joypad = new UIJoypad(100, 100, UIPosition.BOTTOMLEFT);
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		//joypad.setBlankMode();
		
		textbox = new UITextBox(112, 32, UIPosition.TOPLEFT);
		textbox.autoPadding(5.0f, 5.0f, 0.0f, 0.0f);
		textbox.enableTextureMode(someText);
		textbox.setText("Testing!");
		
		UIList.add(healthBar);
		UIList.add(energyBar);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
		UIList.add(textbox);
				
		worldMinX = (-Tile.TILE_SIZE_F * (tileset[0].length / 2)) + (screenW / 2);
		worldMinY = (-Tile.TILE_SIZE_F * (tileset.length / 2)) + (screenH / 2);
		worldMaxX = (Tile.TILE_SIZE_F * (tileset[0].length / 2)) - (screenW / 2);
		worldMaxY = (Tile.TILE_SIZE_F * (tileset.length / 2)) - (screenH / 2);
		
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		updateCameraPosition();
		updateLocalEntities();
		updateLocalTileset();	
				
		//Parser
		Parser parser = new Parser(context);
		try {
			parser.parseLevel();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public void updateAI(Enemy enemy)
	{
		if (Vector2f.sub(enemy.getPos(), player.getPos()).mag() < 100.0f)
			runAgressiveAI(enemy);
		else
			runPassiveAI(enemy);
	}
	
	public void runPassiveAI(Enemy enemy)
	{
		if (enemy.getType() == EnemyType.STALKER)
		{
			enemy.stop();
		}
		else if (enemy.getType() == EnemyType.PATROL)
		{
			
		}
		else if (enemy.getType() == EnemyType.TURRET)
		{
			enemy.setAngle(enemy.getAngle() + 1.5f);
		}
	}
	
	public void runAgressiveAI(Enemy enemy)
	{
		if (enemy.getType() == EnemyType.STALKER)
		{
			//TODO: A* Pathfinding Algorithm
			enemy.moveTo(player.getXPos(), player.getYPos());
			enemy.setAngle((float)Math.toDegrees(Vector2f.sub(enemy.getPos(), player.getPos()).angle()) - 90.0f);
		}
		else if (enemy.getType() == EnemyType.PATROL)
		{
			//TODO: Nodes n' shit
		}
		else if (enemy.getType() == EnemyType.TURRET)
		{
			enemy.setAngle((float)Math.toDegrees(Vector2f.sub(enemy.getPos(), player.getPos()).angle()) - 90.0f);
			
			if (Stopwatch.elapsedTimeMs() - enemy.getLastTime() > enemy.getRandomTime())
			{
				Vector2f directionVec = new Vector2f(enemy.getAngle());
				directionVec.scale(enemy.getHalfSize() + 20.0f);
				AttackBolt attack = new AttackBolt(Vector2f.add(enemy.getPos(), directionVec), directionVec, enemy.getAngle());
				EntityManager.addEntity(attack);
				enemy.setRandomTime((int)(Math.random() * 500) + 500);
				enemy.setLastTime(Stopwatch.elapsedTimeMs());
			}
		}
	}
	
	public void updateCameraPosition()
	{
		//move camera to follow player.
		camPosX = player.getXPos();
		camPosY = player.getYPos();
		
		//camera can't go further than defined level bounds
		if (camPosX < worldMinX)
			camPosX = worldMinX;
			
		else if (camPosX > worldMaxX)
			camPosX = worldMaxX;
		
		if (camPosY < worldMinY)
			camPosY = worldMinY;
		
		else if (camPosY > worldMaxY)
			camPosY = worldMaxY;
	}
	
	public void setGameOverEvent(OnGameOverListener listener)
	{
		gameOverListener = listener;
		triggerList.add(new Trigger(new CauseDoneScaling(player), new EffectEndGame(gameOverListener)));
		triggerList.add(new Trigger(new CausePlayerHealth(0, player), new EffectEndGame(gameOverListener)));
	}
	
	public void gameOver ()
	{
		gameOverListener.onGameOver();
	}
}
