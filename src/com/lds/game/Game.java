package com.lds.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.lds.*;
import com.lds.Enums.*;
import com.lds.game.ai.Node;
import com.lds.game.ai.NodeLink;
import com.lds.game.ai.NodePath;


import com.lds.UI.*;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.trigger.*;

import com.lds.parser.Parser;

public class Game
{
	
	public Level[] levels;

	public int frameInterval;
	public static boolean worldOutdated, windowOutdated;
	
	public ArrayList<Entity> entList;
	public Tile[][] tileset;
	public ArrayList<UIEntity> UIList;
	public ArrayList<Trigger> triggerList;
	public ArrayList<Node> nodeList;
	public EntityManager cleaner;
	
	public ArrayList<Finger> fingerList;
	
	//Camera data
	public static float screenW, screenH;
	public float camPosX;
	public float camPosY;
	
	public float worldMinX, worldMinY, worldMaxX, worldMaxY;
	public int tilesetMinX, tilesetMinY, tilesetMaxX, tilesetMaxY;
	
	//Texture data
	public static Texture tilesetcolors;
	public static Texture tilesetwire;
	public static Texture randomthings;
	public static Texture text;
	public static Texture tilesetworld;
	public static Texture tilesetentities;
	public static Texture joystickout;
	public static Texture joystickin;
	public static Texture buttona;
	public static Texture buttonb;
	public static Texture baricons;
	public static Texture energybarborder;
	public static Texture healthbarborder;
	public Texture someText;
	
	
	//Testing data
	public UIHealthBar healthBar;
	public UIEnergyBar energyBar;
	public UIButton btnA;
	public UIButton btnB;	
	public UIJoypad joypad;
	public UITextBox textbox;
	public Player player;
	///*
	public PhysBall block;
	public PhysBall circle;
	public Button button;
	public Door door;
	public Blob blob1, blob2;
	public PuzzleBox box;
	public PickupHealth health;
	//*/
	//public Sprite spr;
	
	//public Animation spriteAnim;
		
	//Constructors
	public Game (Context context, GL10 gl) 
	{
		fingerList = new ArrayList<Finger>();
		
		tilesetcolors = new Texture(R.drawable.tilesetcolors, 128, 128, 8, 8, context, "tilesetcolors");
		tilesetwire = new Texture(R.drawable.tilesetwire, 128, 128, 8, 8, context, "tilesetwire");
		randomthings = new Texture(R.drawable.randomthings, 256, 256, 8, 8, context, "randomthings");
		text = new Texture(R.drawable.text, 256, 256, 16, 8, context, "text");
		tilesetworld = new Texture(R.drawable.tilesetworld, 512, 256, 16, 8, context, "tilesetworld");
		tilesetentities = new Texture(R.drawable.tilesetentities, 256, 256, 8, 8, context, "tilesetentities");
		joystickout = new Texture(R.raw.joystickout, 64, 64, 1, 1, context, "joystickout");
		joystickin = new Texture(R.raw.joystickin, 32, 32, 1, 1, context, "joystickin");
		buttona = new Texture(R.raw.buttona, 32, 32, 1, 1, context, "buttona");
		buttonb = new Texture(R.raw.buttonb, 32, 32, 1, 1, context, "buttonb");
		baricons = new Texture (R.raw.baricons, 32, 16, 2, 1, context, "baricons");
		energybarborder = new Texture (R.raw.energybarborder, 128, 16, 1, 1, context, "energybarborder");
		healthbarborder = new Texture(R.raw.healthbarborder, 256, 16, 1, 1, context, "healthbarborder");
		
				
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
		tl.loadTexture(tilesetworld);
		tl.loadTexture(tilesetentities);
		tl.loadTexture(joystickout);
		tl.loadTexture(joystickin);
		tl.loadTexture(buttona);
		tl.loadTexture(buttonb);
		tl.loadTexture(baricons);
		tl.loadTexture(energybarborder);
		tl.loadTexture(healthbarborder);
						
		///*		
 		for (int i = 0; i < tileset.length; i++)
		{
			for (int j = 0; j < tileset[0].length; j++)
			{
				tileset[i][j] = new Tile(Tile.TILE_SIZE_F, j, i, tileset[0].length - 1, tileset.length - 1);
				tileset[i][j].enableTilesetMode(tilesetworld, 0, 0);
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
		//tileset[12][6].setAsSlipperyTile();
		//tileset[13][6].setAsSlipperyTile();
		//tileset[11][6].setAsSlipperyTile();
 		
 		for (int i = 0; i < tileset.length; i++)
 		{
 			for (int j = 0; j < tileset[0].length; j++)
 			{
 				if (tileset[i][j].isPit())
 					tileset[i][j].updateBordersPit(tileset, j, i);
 				else if (tileset[i][j].isWall())
 					tileset[i][j].updateBordersWall(tileset, j, i);
 			}
 		}
 		//*/		
		/*//Parser
		Parser parser = new Parser(context, R.xml.tempdata);
		
		entList = parser.entList;
		try 
		{
			parser.parseLevel();
		} 
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
	
		/*tileset = parser.tileset;
		entList.addAll(parser.entList);*/
		
		//  /*CAN DEAL WITH THIS SHIT
		door = new Door (-108.0f, -180.0f);
		door.setAngle(90.0f);
		door.setXScl(0.5f);
		door.enableTilesetMode(tilesetentities, 2, 1);
		entList.add(door);
		
		button = new Button(36.0f, -320.0f);
		button.enableTilesetMode(tilesetentities, 0, 0);
		entList.add(button);
		
		block = new PhysBall(Entity.DEFAULT_SIZE, -215.0f, -425.0f, 0.003f);
		block.enableTilesetMode(tilesetentities, 2, 0);
		//float[] initGM = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
		//float[] interpGM = {0.1f, 0.1f, 0.1f, 1.0f, 0.3f, 0.8f, 0.9f, 1.0f, 0.1f, 0.8f, 0.1f, 1.0f, 0.9f, 0.2f, 0.1f, 1.0f};
		//block.enableGradientMode(initGM);
		//block.setColorInterpSpeed(0.01f);
		//block.initGradientInterp(interpGM);
		entList.add(block);
		
		/*blob1 = new Blob(-250.0f, 0.0f, AIType.STALKER);
		blob1.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(blob1);
		
		blob2 = new Blob(-215.0f, -400.0f, AIType.TURRET);
		blob2.enableTilesetMode(tilesetwire, 2, 2);
		entList.add(blob2);
		final NodePath np = new NodePath();
		np.add(new Node(-215, -400));
		np.add(new Node(-215, -300));
		np.add(new Node(-100, -300));
		blob2.setPatrolPath(np);*/

		Button button1 = new Button(108.0f, 0.0f);
		button1.enableTilesetMode(tilesetentities, 0, 0);
		entList.add(button1);
				
		Button button2 = new Button(-324.0f, 0.0f);
		button2.enableTilesetMode(tilesetentities, 0, 0);
		entList.add(button2);
		

		PhysBlock block1 = new PhysBlock(64.0f, -100.0f, 0.0f, 0.03f);
		block1.enableTilesetMode(tilesetentities, 3, 0);
		entList.add(block1);
		 
		PhysBlock block2 = new PhysBlock(50, 0, 90, 0.03f);
		block2.enableTilesetMode(tilesetentities, 3, 0);
		entList.add(block2);

		/*SpikeBall wall = new SpikeBall(35, -200, -250, true, true, 15, 500, 0.0f, 0.0f, 0, -300, 1);
		wall.enableTilesetMode(tilesetwire, 1, 2);
		entList.add(wall);*/
		
		/*Cannon cannon = new Cannon(35, -100, -425, 90, 1, 1, true, false, true, 5, 5);
		cannon.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(cannon);*/
		
		Teleporter tele1 = new Teleporter(40,0,-340);
		tele1.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(tele1);
		
		Teleporter tele2 = new Teleporter(40,-250,-340);
		tele2.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(tele2);
		
		Teleporter tele3 = new Teleporter(40,-250,-100);
		tele3.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(tele3);
		
		TeleporterLinker teleLink = new TeleporterLinker(tele1, tele2, tele3, false, true);
		
		/*spriteAnim = new Animation(tilesetwire, 0, 7, 7, 0, 3000);
		spr = new Sprite(50, -100, 100, 45, 1, 1, spriteAnim);
		spr.enableTextureMode(tilesetwire);
		entList.add(spr);*/

		/*box = new PuzzleBox(-120.0f, -400.0f, 0.0f, false, true);
		entList.add(box);*/

		
		player = new Player(-108.0f, -450.0f, 0.0f);
		player.enableTilesetMode(tilesetwire, 1, 0);
		entList.add(player);
		player.enableUserControl();
		
		/*
		health = new PickupHealth(50, -108.0f, -250.0f);
		health.enableColorMode(0, 255, 255, 255);
		entList.add(health);
		*/
		//spr = new Sprite(30.0f, -108.0f, -300.0f, 45.0f, 1.0f, 1.0f, 10, 90, 1, spriteAnim);
		//entList.add(spr);
		
		nodeList = new ArrayList<Node>();
		nodeList.add(new Node(-105.0f, -225.0f));
		nodeList.add(new Node(-105.0f, -145.0f));
		nodeList.add(new Node(-350.0f, 0.0f));
		nodeList.add(new Node(30.0f, 0.0f));
		nodeList.add(new Node(-105.0f, 130.0f));
		nodeList.add(new Node(-105.0f, 320.0f));
		nodeList.get(0).addNodeLink(nodeList.get(1));
		nodeList.get(1).addNodeLink(nodeList.get(2));
		nodeList.get(1).addNodeLink(nodeList.get(3));
		nodeList.get(2).addNodeLink(nodeList.get(4));
		nodeList.get(3).addNodeLink(nodeList.get(4));
		nodeList.get(4).addNodeLink(nodeList.get(5));

		nodeList.get(0).deactivateLinks(nodeList.get(1));
		CauseAND bridgeAND = new CauseAND(new CauseButton(button1), new CauseButton(button2));
		
		
		triggerList.add(new Trigger(new CauseButton(button), new EffectAND(new EffectDoor(door), new EffectDeactivateNodeLink(nodeList.get(0), nodeList.get(1)))));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][6])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][7])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][6])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][7])));
		
		//TODO UIHealthBar is a UIEntity sub that contains 2 UIImages and a UIProgressBar (which will no longer be abstract)
		healthBar = new UIHealthBar(248.0f, 8.0f, UIPosition.TOPRIGHT, Direction.LEFT, player);
		healthBar.setTopPad(9.0f);
		healthBar.setRightPad(9.0f);
		healthBar.autoPadding(9, 0, 0, 9);
		healthBar.enableColorMode(0.8f, 0.0f, 0.0f, 0.9f);
		healthBar.setValue(100);
		
		UIImage healthBarCover = new UIImage(256, 16, UIPosition.TOPRIGHT);
		healthBarCover.setTopPad(5.0f);
		healthBarCover.setRightPad(5.0f);
		healthBarCover.autoPadding(5, 0, 0, 5);
		healthBarCover.enableTextureMode(healthbarborder);
		
		UIImage healthIcon = new UIImage (16, 16, UIPosition.TOPRIGHT);
		healthIcon.setTopPad(5.0f);
		healthIcon.setRightPad(266.0f);
		healthIcon.autoPadding(5, 0, 0, 266);
		healthIcon.enableTilesetMode(baricons, 0, 0);
		
		energyBar = new UIEnergyBar(184.0f, 8.0f, UIPosition.TOPRIGHT, Direction.LEFT, player);
		energyBar.setTopPad(30.0f);
		energyBar.setRightPad(9.0f);
		energyBar.autoPadding(30, 0, 0, 9);
		energyBar.enableColorMode(0.0f, 0.0f, 0.8f, 0.9f);
		energyBar.setValue(100);
		
		UIImage energyBarCover = new UIImage(192, 16, UIPosition.TOPRIGHT);
		energyBarCover.setTopPad(26.0f);
		energyBarCover.setRightPad(5.0f);
		energyBarCover.autoPadding(26, 0, 0, 5);
		energyBarCover.enableTextureMode(energybarborder);
		
		UIImage energyIcon = new UIImage (16, 16, UIPosition.TOPRIGHT);
		energyIcon.setTopPad(26.0f);
		energyIcon.setRightPad(202.0f);
		energyIcon.autoPadding(26, 0, 0, 202);
		energyIcon.enableTilesetMode(baricons, 1, 0);
		
		btnA = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnA.autoPadding(0.0f, 0.0f, 5.0f, 90.0f);
		//btnA.enableColorMode(65, 200, 65, 128);
		btnA.enableTextureMode(buttona);
		btnA.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		//btnB.enableColorMode(200, 65, 65, 100);
		btnB.enableTextureMode(buttonb);
		btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		
		joypad = new UIJoypad(.45f, .45f, UIPosition.BOTTOMLEFT, player.getAngle());
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.enableTextureMode(joystickout);
		
		textbox = new UITextBox(112, 32, UIPosition.TOPLEFT);
		textbox.autoPadding(5.0f, 5.0f, 0.0f, 0.0f);
		textbox.enableTextureMode(someText);
		textbox.setText("Testing!");
		
		UIList.add(healthBar);
		UIList.add(healthBarCover);
		UIList.add(healthIcon);
		UIList.add(energyBar);
		UIList.add(energyBarCover);
		UIList.add(energyIcon);
		UIList.add(btnA);
		UIList.add(btnB);
		UIList.add(joypad);
		UIList.add(textbox);
				
		worldMinX = (-Tile.TILE_SIZE_F * (tileset[0].length / 2)) + (screenW / 2); //TODO fix odd-number tilesets by changing the array length to a float before dividing.
		worldMinY = (-Tile.TILE_SIZE_F * (tileset.length / 2)) + (screenH / 2);
		worldMaxX = (Tile.TILE_SIZE_F * (tileset[0].length / 2)) - (screenW / 2);
		worldMaxY = (Tile.TILE_SIZE_F * (tileset.length / 2)) - (screenH / 2);
		
		//TODO take into account AI, perhaps render every time it chooses a new point to go to?
		updateCameraPosition();
		updateRenderedEnts();

		updateRenderedTileset();
		 
		System.gc();
	}
	
	public void updateRenderedEnts()
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
	
	public void updateRenderedTileset()
	{
		float minX, maxX, minY, maxY, tilesetHalfWidth, tilesetHalfHeight;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenH / 2);
		maxY = camPosY + (screenH / 2);
		
		tilesetHalfWidth = tileset[0].length * Tile.TILE_SIZE_F / 2;
		tilesetHalfHeight = tileset.length * Tile.TILE_SIZE_F / 2;
		
		tilesetMinX = (int)(minX + tilesetHalfWidth) / Tile.TILE_SIZE;
		tilesetMaxX = (int)((Math.ceil(maxX + tilesetHalfWidth) - 1) / Tile.TILE_SIZE_F);
		tilesetMinY = (int)((Math.abs(maxY - tilesetHalfHeight) - 1) / Tile.TILE_SIZE_F);
		tilesetMaxY = (int)((Math.ceil(Math.abs(minY - tilesetHalfHeight)) - 1) / Tile.TILE_SIZE_F);
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
	
	public Tile nearestTile(Entity ent)
	{	
		//TODO Fix return null when offscreen
		final float tilesetHalfWidth = tileset[0].length * Tile.TILE_SIZE_F / 2;
		final float tilesetHalfHeight = tileset.length * Tile.TILE_SIZE_F / 2;
		
		final int x = (int)(ent.getXPos() + tilesetHalfWidth) / Tile.TILE_SIZE;
		final int y = (int)(Math.abs(ent.getYPos() - tilesetHalfHeight)) / Tile.TILE_SIZE;
		
		if (x < tileset[0].length && x >= 0 && y < tileset.length && y >= 0)
		{
			return tileset[y][x];
		}
		
		return null;
		
	}
	
	public void runAI(Enemy enemy)
	{
		if (Vector2f.sub(enemy.getPos(), player.getPos()).mag() <  Enemy.OUTER_RADIUS)
		{
			if (enemy.isAgressive())
				runAgressiveAI(enemy);
			else
				runBecomeAgressiveAI(enemy);
		}
		else
		{
			if (enemy.isAgressive())
				runBecomePassiveAI(enemy);
			else
				runPassiveAI(enemy);
		}
	}
	
	public void runAgressiveAI(Enemy enemy)
	{
		if (enemy.getType() == AIType.STALKER || enemy.getType() == AIType.PATROL)
		{
			if (enemy.getPathToPlayer() != null)
			{
				/*if (enemy.isColliding())
				{
					if (enemy.getPlayerPathLocation() >= enemy.getPathToPlayer().getSize() - 2)
					{
						enemy.stop();
						runBecomeAgressiveAI(enemy);
						if (enemy.getPathToPlayer() == null)
							return;
					}
					else
					{
						enemy.stop();
						Node lastNode = enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation());
						Node nextNode = enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1);
						//lastNode.deactivateLinks(nextNode);
						runBecomeAgressiveAI(enemy);
						if (enemy.getPathToPlayer() == null)
							return;
						//lastNode.activateLinks(nextNode);
					}
					enemy.stop();
					runBecomeAgressiveAI(enemy);
					if (enemy.getPathToPlayer() == null)
						return;
					enemy.setColliding(false);
				}*/
				//if close enough to player, move towards him. hang back and shoot if too close enough
				if (enemy.getPathToPlayer().getSize() == 2 || pathIsClear(new Node(enemy.getPos()), new Node(player.getPos())))
				{
					//shoot, on timer
					if (Stopwatch.elapsedTimeMs() - enemy.getLastTime() > enemy.getRandomTime())
					{
						Vector2f directionVec = new Vector2f(enemy.getAngle());
						AttackBolt attack = new AttackBolt(Vector2f.add(enemy.getPos(), directionVec), directionVec.scale(15), enemy.getAngle());
						attack.ignore(enemy);
						EntityManager.addEntity(attack);
						enemy.setRandomTime((int)(Math.random() * 500) + 500);
						enemy.setLastTime(Stopwatch.elapsedTimeMs());
					}
					//move to player
					if (Vector2f.sub(enemy.getPos(), player.getPos()).mag() <  Enemy.INNER_RADIUS)
					{
						enemy.stop();
					}
					else
					{
						enemy.stop();
						enemy.rotateTo(Vector2f.sub(player.getPos(), enemy.getPos()).angleDeg());
						enemy.moveTo(player.getPos());
						runBecomeAgressiveAI(enemy);
					}
				}
				else//if not close to player yet
				{
					//if at the next node, update location
					if (enemy.getPos().approxEquals(enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1).getPos(), 2.0f))
					{
						enemy.stop();
						if (enemy.getPlayerPathLocation() + 1 == enemy.getPathToPlayer().getSize() - 1)
						{
							runBecomeAgressiveAI(enemy);
							return;
						}
						else
							enemy.setPlayerPathLocation(enemy.getPlayerPathLocation() + 1);
					}
					Vector2f nextNodePos = enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1).getPos();
					enemy.moveTo(nextNodePos);
					enemy.rotateTo(Vector2f.sub(nextNodePos, enemy.getPos()).angleDeg());
				}
			}
			else
			{
				runBecomeAgressiveAI(enemy);
			}
		}
		else if (enemy.getType() == AIType.TURRET)
		{
			float towardsPlayerAngle = (float)Vector2f.sub(player.getPos(), enemy.getPos()).angleDeg();
			if (enemy.getAngle() > towardsPlayerAngle + 5.0f || enemy.getAngle() < towardsPlayerAngle - 5.0f)
				enemy.rotateTo(towardsPlayerAngle);
			else
				enemy.setAngle(towardsPlayerAngle);
			
			if (Stopwatch.elapsedTimeMs() - enemy.getLastTime() > enemy.getRandomTime())
			{
				Vector2f directionVec = new Vector2f(enemy.getAngle());
				final AttackBolt attack = new AttackBolt(Vector2f.add(enemy.getPos(), directionVec), directionVec.scale(15), enemy.getAngle());
				attack.ignore(enemy);
				EntityManager.addEntity(attack);
				enemy.setRandomTime((int)(Math.random() * 500) + 500);
				enemy.setLastTime(Stopwatch.elapsedTimeMs());
			}
		}
	}
	
	public void runBecomeAgressiveAI(Enemy enemy)
	{
		enemy.setAgressive(true);
		enemy.setOnPatrol(false);
		if (enemy.getType() == AIType.STALKER)
		{	
			enemy.setPathToPlayer(getPathToPlayer(enemy));
			enemy.setPlayerPathLocation(0);
		}
		else if (enemy.getType() == AIType.PATROL)
		{
			enemy.stop();
			float angleToPlayer = (float)Vector2f.sub(player.getPos(), enemy.getPos()).angleDeg();
			if (enemy.getAngle() == angleToPlayer)
			{
				enemy.setAngle(angleToPlayer);
				if (!enemy.isMoving)
					enemy.moveTo(player.getPos());
				enemy.setDoneRotating(true);
			}
			else
			{
				enemy.rotateTo(angleToPlayer);
				enemy.setDoneRotating(false);
			}
			
			//pathfinding stuff
			enemy.setPathToPlayer(getPathToPlayer(enemy));
			enemy.setPlayerPathLocation(0);
		}
		else if (enemy.getType() == AIType.TURRET)
		{
			
		}
	}
	
	public void runPassiveAI(Enemy enemy)
	{
		if (enemy.getType() == AIType.STALKER)
		{
			enemy.stop();
		}
		else if (enemy.getType() == AIType.PATROL)
		{
			if (enemy.isOnPatrol())
			{
				Node nextNode;
				if (enemy.getPatrolPathLocation() == enemy.getPatrolPath().getSize() - 1)
					nextNode = enemy.getPatrolPathNode(0);
				else
					nextNode = enemy.getPatrolPathNode(enemy.getPatrolPathLocation() + 1);
				
				if (enemy.getPos().equals(nextNode.getPos()))
				{
					enemy.setDoneRotating(false);
					if (enemy.getPatrolPathLocation() == enemy.getPatrolPath().getSize() - 1)
						enemy.setPatrolPathLocation(0);
					else
						enemy.setPatrolPathLocation(enemy.getPatrolPathLocation() + 1);
				}
				
				float angleToNode = (float)Vector2f.sub(nextNode.getPos(), enemy.getPos()).angleDeg();
				
				if (enemy.getAngle() == angleToNode)
					enemy.setDoneRotating(true);
				else
					enemy.setDoneRotating(false);
				
				if (enemy.isDoneRotating())
				{
					enemy.setAngle(angleToNode);
					enemy.moveTo(nextNode.getPos());
				}
				else
				{
					enemy.rotateTo(angleToNode);
				}
			}
			else
			{
				enemy.setDoneRotating(false);
				runBecomePassiveAI(enemy);
			}
		}
		else if (enemy.getType() == AIType.TURRET)
		{
			enemy.setAngle(enemy.getAngle() + 1.5f);
		}
	}
	
	public void runBecomePassiveAI(Enemy enemy)
	{
		enemy.setAgressive(false);
		if (enemy.getType() == AIType.STALKER)
		{
			enemy.stop();
		}
		else if (enemy.getType() == AIType.PATROL)
		{
			enemy.stop();
			Node closestNode = enemy.getClosestPatrolPathNode();
			
			if (enemy.getPos().approxEquals(closestNode.getPos(), 2.0f))
			{
				enemy.setOnPatrol(true);
				enemy.setPatrolPathLocation(enemy.getClosestPatrolPathNodeIndex());
			}
			else
			{
				float angleToNode = (float)Vector2f.sub(closestNode.getPos(), enemy.getPos()).angleDeg();
				if (enemy.getAngle() == angleToNode)
				{
					enemy.setDoneRotating(true);
					enemy.setAngle(angleToNode);
					enemy.moveTo(closestNode.getPos());
				}
				else
				{
					enemy.setDoneRotating(false);
					enemy.rotateTo(angleToNode);
				}
			}
		}
		else if (enemy.getType() == AIType.TURRET)
		{
			
		}
	}
	
	public void setGameOverEvent(OnGameOverListener listener)
	{
		triggerList.add(new Trigger(new CauseDoneScaling(player), new EffectEndGame(listener)));
		triggerList.add(new Trigger(new CausePlayerHealth(0, player), new EffectEndGame(listener)));
	}
	
	public void updateFingers()
	{
		if(player.userHasControl())
		{
			for(Finger f : fingerList)
			{
				f.update();
			}
		}
	}
	
	public NodePath getPathToPlayer (final Entity ent)
	{
		final Node goalNode = new Node(player.getPos());
		final Node startNode = new Node(ent.getPos());
		
		//if enemy can go straight towards player
		if (pathIsClear(startNode, goalNode))
			return new NodePath(startNode, goalNode);
		
		//checks if player is reachable
		boolean goalReachable = false;
		boolean startConnected = false;
		for (Node node : nodeList)
		{
			if (pathIsClear(goalNode, node))
			{
				goalNode.addNodeLink(node);
				goalReachable = true;
			}
			if (pathIsClear(startNode, node))
			{
				startNode.addNodeLink(node);
				startConnected = true;
			}
		}
		
		if (!goalReachable || !startConnected)
			return null;

		//int previousListSize = 1;
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		Node lowestF = startNode;
		closedList.add(startNode);
		final Vector2f startHVec = Vector2f.sub(startNode.getPos(), goalNode.getPos());
		startNode.setH(startHVec.mag());
		startNode.setF(startNode.getH());
		
		boolean givenUp = true;
		while (!openList.contains(goalNode) && !closedList.contains(goalNode))
		{
			givenUp = true;
			for (int i = 0; i < lowestF.getNodeCount(); i++)
			{
				final Node node = lowestF.getLinkedNode(i);
				if (!closedList.contains(node) && lowestF.getNodeLink(i).isActive())
				{
					if (openList.contains(node))
					{
						float newG = lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().mag();
						if (node.getG() > newG)
						{
							node.setG(newG);
							node.setF(node.getG() + node.getH());
							node.setParentNode(lowestF);
						}
					}
					else
					{
						final Vector2f hVec = Vector2f.sub(node.getPos(), goalNode.getPos());
						node.setG(lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().mag());
						node.setH(hVec.mag());
						node.setF(node.getG() + node.getH());
						node.setParentNode(lowestF);
						openList.add(node);
						givenUp = false;
					}
				}
			}
			lowestF = openList.get(0);
			for (int i = 1; i < openList.size(); i++)
			{
				if (openList.get(i).getF() < lowestF.getF())
					lowestF = openList.get(i);
			}
			openList.remove(lowestF);
			closedList.add(lowestF);
			
			if (givenUp)
				break;
		}
		
		if (openList.contains(goalNode) || closedList.contains(goalNode))
		{
			//find nodePath from goal to start
			NodePath path = new NodePath(goalNode);
			Node currentNode = goalNode;
			while (currentNode != startNode)
			{
				currentNode = currentNode.getParentNode();
				path.add(currentNode);
			}
			//reverse path
			path.reverse();
			for (Node node : nodeList)
				node.setParentNode(null);
			openList.clear();
			closedList.clear();
			startNode.clearLinks();
			goalNode.clearLinks();
			return path;
		}
		else
		{
			for (Node node : nodeList)
				node.setParentNode(null);
			openList.clear();
			closedList.clear();
			startNode.clearLinks();
			goalNode.clearLinks();
			return null;
		}
	}
	
	public boolean pathIsClear(final Vector2f startVec, final Vector2f endVec)
	{
		final Vector2f pathVec = Vector2f.sub(endVec, startVec).normalize();
		final Vector2f pathNormal = Vector2f.getNormal(pathVec);
		final float normProj = startVec.dot(pathNormal);
		final float startProj = startVec.dot(pathVec);
		final float endProj = endVec.dot(pathVec);
		
		for (final Entity ent : entList)
		{
			float entProj = ent.getPos().dot(pathVec);
			if (ent.isSolid() && ent.willCollide() && Math.abs(ent.getPos().dot(pathNormal) - normProj) < ent.getHalfSize() * 1.5f && ((entProj > startProj && entProj < endProj) || (entProj < startProj && entProj > endProj)))
				return false;
		}
		for (final Tile[] ta : tileset)
		{
			for (final Tile tile : ta)
			{
				float tileProj = tile.getPos().dot(pathVec);
				if ((tile.isWall() || tile.isPit()) && Math.abs(tile.getPos().dot(pathNormal) - normProj) < tile.getHalfSize() * 1.5f && ((tileProj > startProj && tileProj < endProj) || (tileProj < startProj && tileProj > endProj)))
					return false;
			}
		}
		
		return true;
	}
	
	public boolean pathIsClear(final Node startNode, final Node endNode)
	{
		return pathIsClear(startNode.getPos(), endNode.getPos());
	}
	
	public void updateTriggers()
	{
		for (Trigger t : triggerList)
			t.update();
	}
	
	public void updatePlayerPos()
	{
		//move player
		player.setAngle(joypad.getInputAngle());
		player.addPos(joypad.getInputVec().scale((Stopwatch.elapsedTimeMs() - frameInterval) * (player.getMoveSpeed() / 1000)));
		joypad.clearInputVec();
		
		//move heldObject if neccessary
		if (player.isHoldingObject())
			player.updateHeldObjectPosition();
	}
	
	public void renderTileset(GL10 gl)
	{
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tilesetworld.getTexture());
				
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		//TODO don't iterate through all and check if visible, have bounds available
		for (int i = tilesetMinY; i <= tilesetMaxY; i++)
		{
			for (int j = tilesetMinX; j <= tilesetMaxX; j++)
			{
				tileset[i][j].updateTextureVBO(gl);
				tileset[i][j].draw(gl);
			}
		}
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public static boolean arrayListContains(ArrayList<Entity> entList, Entity ent)
	{
		final int size = entList.size();
		for (int i = 0; i < size; i++)
		{
			if (ent == entList.get(i))
				return true;
		}
		return false;
	}
}
