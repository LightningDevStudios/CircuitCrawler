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
	
	//public Level[][] GameLevels;

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
		tileset[12][6].setAsSlipperyTile();
		tileset[13][6].setAsSlipperyTile();
		tileset[11][6].setAsSlipperyTile();
 		
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
		door.enableTilesetMode(tilesetwire, 0, 2);
		entList.add(door);
		
		button = new Button(36.0f, -320.0f);
		button.enableTilesetMode(randomthings, 0, 0);
		entList.add(button);
		
		block = new PhysBall(Entity.DEFAULT_SIZE, -215.0f, -350.0f);
		block.enableTilesetMode(tilesetwire, 1, 2);
		//float[] initGM = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
		//float[] interpGM = {0.1f, 0.1f, 0.1f, 1.0f, 0.3f, 0.8f, 0.9f, 1.0f, 0.1f, 0.8f, 0.1f, 1.0f, 0.9f, 0.2f, 0.1f, 1.0f};
		//block.enableGradientMode(initGM);
		//block.setColorInterpSpeed(0.01f);
		//block.initGradientInterp(interpGM);
		entList.add(block);
		
		blob1 = new Blob(-300.0f, 0.0f, AIType.STALKER);
		blob1.enableTilesetMode(tilesetwire, 2, 2);
		entList.add(blob1);
		
		/*blob2 = new Blob(0.0f, 0.0f, AIType.TURRET);
		blob2.enableTilesetMode(tilesetwire, 2, 2);
		entList.add(blob2);*/
				
		Button button1 = new Button(108.0f, 0.0f);
		button1.enableTilesetMode(randomthings, 0, 0);
		entList.add(button1);
				
		Button button2 = new Button(-324.0f, 0.0f);
		button2.enableTilesetMode(randomthings, 0, 0);
		entList.add(button2);
		

		PhysBlock block1 = new PhysBlock(50, -200, 90);
		block1.enableTilesetMode(tilesetwire, 2, 2);
		entList.add(block1);
		 
		PhysBlock block2 = new PhysBlock(50, 0, 90);
		block2.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(block2);

		/*SpikeBall wall = new SpikeBall(35, -200, -250, true, true, 15, 500, 0.0f, 0.0f, 0, -300, 1);
		wall.enableTilesetMode(tilesetwire, 1, 2);
		//wall.scale(1.0f,2.0f);
		entList.add(wall);*/
		
		Cannon cannon = new Cannon(35, -150, -340, 90, 1, 1, true, false, true, 5);
		
		/*Cannon cannon = new Cannon(35, -100, -300, 90, 1, 1, true, false, true, 5);
>>>>>>> refs/remotes/origin/devin_reed_master
		cannon.enableTilesetMode(tilesetwire, 2, 1);
		entList.add(cannon);*/
		
		/*
		MovingWall wall2 = new MovingWall(35, -150, -300, true, true, 5, 500, 0.0f, 0.0f, -112.5f, -300, -1);
		wall2.enableTilesetMode(tilesetwire, 1, 3);
		//wall2.scale(1.0f,2.0f);
		entList.add(wall2);
	    */
		
		/*spriteAnim = new Animation(tilesetwire, 0, 7, 7, 0, 3000);
		spr = new Sprite(50, -100, 100, 45, 1, 1, spriteAnim);
		spr.enableTextureMode(tilesetwire);
		entList.add(spr);*/

		box = new PuzzleBox(-120.0f, -400.0f, 0.0f, false, true);
		entList.add(box);

		
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
		nodeList.add(new Node(-200.0f, 0.0f));
		nodeList.add(new Node(30.0f, 0.0f));
		nodeList.add(new Node(-105.0f, 130.0f));
		nodeList.add(new Node(-105.0f, 320.0f));
		nodeList.get(0).addNodeLink(nodeList.get(1));
		nodeList.get(1).addNodeLink(nodeList.get(2));
		nodeList.get(1).addNodeLink(nodeList.get(3));
		nodeList.get(2).addNodeLink(nodeList.get(4));
		nodeList.get(3).addNodeLink(nodeList.get(4));
		nodeList.get(4).addNodeLink(nodeList.get(5));
		
		CauseAND bridgeAND = new CauseAND(new CauseButton(button1), new CauseButton(button2));
		
		triggerList.add(new Trigger(new CauseButton(button), new EffectDoor(door)));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][6])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[4][7])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][6])));
		triggerList.add(new Trigger(bridgeAND, new EffectRaiseBridge(tileset[5][7])));
		
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
		
		joypad = new UIJoypad(100, 100, UIPosition.BOTTOMLEFT, player.getAngle());
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		
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
		if (Vector2f.sub(enemy.getPos(), player.getPos()).mag() < 500.0f)
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
		if (enemy.getType() == AIType.STALKER)
		{
			if (enemy.getPathToPlayer() != null)
			{
				if (enemy.isDoneRotating())
				{
					enemy.moveTo(enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1).getPos());
					enemy.setAngle((float)Vector2f.sub(enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1).getPos(), enemy.getPos()).angleDeg());
				}
				else
				{
					runBecomeAgressiveAI(enemy);
				}
			}
		}
		else if (enemy.getType() == AIType.PATROL)
		{
			
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
				directionVec.scale(enemy.getHalfSize() + 20.0f);
				AttackBolt attack = new AttackBolt(Vector2f.add(enemy.getPos(), directionVec), directionVec, enemy.getAngle());
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
			
		}
		else if (enemy.getType() == AIType.PATROL)
		{
			enemy.stop();
			float angleToPlayer = (float)Vector2f.sub(player.getPos(), enemy.getPos()).angleDeg();
			if (enemy.getAngle() == angleToPlayer)
			{
				enemy.setAngle(angleToPlayer);
				enemy.moveTo(player.getPos());
				enemy.setDoneRotating(true);
			}
			else
			{
				enemy.rotateTo(angleToPlayer);
				enemy.setDoneRotating(false);
			}
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
			
			if (enemy.getPos().equals(closestNode.getPos()))
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
	
	public NodePath getPathToPlayer (Entity ent)
	{
		Node goalNode = new Node(player.getPos());
		Node startNode = new Node(ent.getPos());
		
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

		int previousListSize = 1;
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		Node lowestF = startNode;
		closedList.add(startNode);
		startNode.setH(Vector2f.sub(startNode.getPos(), goalNode.getPos()).mag());
		startNode.setF(startNode.getH());
		
		while (!openList.contains(goalNode))
		{
			for (int i = 0; i < lowestF.getNodeCount(); i++)
			{
				Node node = lowestF.getLinkedNode(i);
				if (!closedList.contains(node) && lowestF.getNodeLink(i).isActive())
				{
					if (openList.contains(node))
					{
						float newG = lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().mag();
						if (node.getG() > newG)
						{
							node.setG(newG);
							node.setParentNode(lowestF);
						}
					}
					node.setG(lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().mag());
					node.setH(Vector2f.sub(node.getPos(), goalNode.getPos()).mag());
					node.setF(node.getG() + node.getH());
					node.setParentNode(lowestF);
					openList.add(node);
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
			
			if (previousListSize == openList.size() + closedList.size())
				break;
			else
				previousListSize = openList.size() + closedList.size();
		}
		
		if (openList.contains(goalNode))
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
			return path;
		}
		else
			return null;
	}
	
	public boolean pathIsClear(final NodeLink link)
	{
		Vector2f pathVec = link.getNodeVec().normalize();
		Vector2f pathNormal = Vector2f.getNormal(pathVec);
		float normProj = link.getThisNode().getPos().dot(pathNormal);
		float startProj = link.getThisNode().getPos().dot(pathVec);
		float endProj = link.getLinkedNode().getPos().dot(pathVec);
		
		for (final Entity ent : entList)
		{
			float entProj = ent.getPos().dot(pathVec);
			if (ent.isSolid() && ent.willCollide() && Math.abs(ent.getPos().dot(pathNormal) - normProj) < ent.getHalfSize() && ((entProj > startProj && entProj < endProj) || (entProj < startProj && entProj > endProj)))
				return false;
		}
		for (final Tile[] ta : tileset)
		{
			for (final Tile tile : ta)
			{
				float tileProj = tile.getPos().dot(pathVec);
				if ((tile.isWall() || tile.isPit())&& Math.abs(tile.getPos().dot(pathNormal) - normProj) < tile.getHalfSize() && ((tileProj > startProj && tileProj < endProj) || (tileProj < startProj && tileProj > endProj)))
					return false;
			}
		}
		
		
		return true;
	}
	
	public boolean pathIsClear(final Node startNode, final Node endNode)
	{
		return pathIsClear(new NodeLink(startNode, endNode));
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
}
