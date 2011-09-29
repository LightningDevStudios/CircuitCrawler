package com.lds.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.lds.*;
import com.lds.Enums.*;
import com.lds.game.ai.Node;
import com.lds.game.ai.NodePath;
import com.lds.UI.*;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.trigger.*;

import com.lds.math.Vector2;
import com.lds.parser.Parser;

public class Game
{
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
	//\TODO TextureManager class
	public static Texture tilesetwire;
	public static Texture text;
	public static Texture tilesetworld;
	public static Texture tilesetentities;
	public static Texture baricons;
	
	//Testing data
	public UIButton btnA;
	public UIButton btnB;	
	public UIJoypad joypad;
	public Player player;
	
	//Constructors
	public Game (Context context, GL10 gl, int levelId) 
	{		
		tilesetwire = new Texture(R.drawable.tilesetwire, 128, 128, 8, 8, context, "tilesetwire");
		text = new Texture(R.drawable.text, 256, 256, 16, 8, context, "text");
		tilesetworld = new Texture(R.raw.tilesetworld, 512, 256, 16, 8, context, "tilesetworld");
		tilesetentities = new Texture(R.raw.tilesetentities, 256, 256, 8, 8, context, "tilesetentities");
		baricons = new Texture (R.raw.baricons, 32, 16, 2, 1, context, "baricons");
		
		final Texture joystickout = new Texture(R.raw.joystickout, 64, 64, 1, 1, context, "joystickout");
		final Texture joystickin = new Texture(R.raw.joystickin, 32, 32, 1, 1, context, "joystickin");
		final Texture buttona = new Texture(R.raw.buttona, 32, 32, 1, 1, context, "buttona");
		final Texture buttonb = new Texture(R.raw.buttonb, 32, 32, 1, 1, context, "buttonb");
		final Texture energybarborder = new Texture (R.raw.energybarborder, 128, 16, 1, 1, context, "energybarborder");
		final Texture healthbarborder = new Texture(R.raw.healthbarborder, 256, 16, 1, 1, context, "healthbarborder");
		
				
		entList = new ArrayList<Entity>();
		UIList = new ArrayList<UIEntity>();
		triggerList = new ArrayList<Trigger>();
		fingerList = new ArrayList<Finger>();
		tileset = new Tile[16][16];
		cleaner = new EntityManager();
		
		StringRenderer sr = StringRenderer.getInstance();
		TextureLoader.getInstance().initialize(gl);
		sr.loadTextTileset(text);
				
		SoundPlayer.getInstance().initialize(context);
		
		TextureLoader tl = TextureLoader.getInstance();
		
		tl.loadTexture(tilesetwire);
		tl.loadTexture(tilesetworld);
		tl.loadTexture(tilesetentities);
		
		tl.loadTexture(joystickout);
		tl.loadTexture(joystickin);
		tl.loadTexture(buttona);
		tl.loadTexture(buttonb);
		tl.loadTexture(baricons);
		tl.loadTexture(energybarborder);
		tl.loadTexture(healthbarborder);
		 		
		//Parser
		Parser parser = new Parser(context, levelId);
		
		try	
		{ 
			parser.parseLevel(); 
		} 
		catch (Exception e) { e.printStackTrace(); } 
	
		tileset = parser.tileset;
		entList.addAll(parser.entList);
		triggerList.addAll(parser.triggerList);
		
		player = parser.player;
		entList.add(player);
		
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
		
		nodeList = parser.nodeList;
		
		
		//\TODO UIHealthBar is a UIEntity sub that contains 2 UIImages and a UIProgressBar (which will no longer be abstract)
		UIHealthBar healthBar = new UIHealthBar(246.0f, 8.0f, UIPosition.TOPRIGHT, Direction.LEFT, player);
		healthBar.setTopPad(9.0f);
		healthBar.setRightPad(10.0f);
		healthBar.autoPadding(9, 0, 0, 10);
		healthBar.enableColorMode(0.8f, 0.0f, 0.0f, 0.9f);
		healthBar.setValue(100);
		UIList.add(healthBar);
		
		UIImage healthBarCover = new UIImage(256, 16, UIPosition.TOPRIGHT);
		healthBarCover.setTopPad(5.0f);
		healthBarCover.setRightPad(5.0f);
		healthBarCover.autoPadding(5, 0, 0, 5);
		healthBarCover.enableTextureMode(healthbarborder);
		UIList.add(healthBarCover);
		
		UIImage healthIcon = new UIImage (16, 16, UIPosition.TOPRIGHT);
		healthIcon.setTopPad(5.0f);
		healthIcon.setRightPad(266.0f);
		healthIcon.autoPadding(5, 0, 0, 266);
		healthIcon.enableTilesetMode(baricons, 0, 0);
		UIList.add(healthIcon);
		
		UIEnergyBar energyBar = new UIEnergyBar(118.0f, 8.0f, UIPosition.TOPRIGHT, Direction.LEFT, player);
		energyBar.setTopPad(30.0f);
		energyBar.setRightPad(10.0f);
		energyBar.autoPadding(30, 0, 0, 10);
		energyBar.enableColorMode(0.0f, 0.0f, 0.8f, 0.9f);
		energyBar.setValue(100);
		UIList.add(energyBar);
		
		UIImage energyBarCover = new UIImage(128, 16, UIPosition.TOPRIGHT);
		energyBarCover.setTopPad(26.0f);
		energyBarCover.setRightPad(5.0f);
		energyBarCover.autoPadding(26, 0, 0, 5);
		energyBarCover.enableTextureMode(energybarborder);
		UIList.add(energyBarCover);
		
		UIImage energyIcon = new UIImage (16, 16, UIPosition.TOPRIGHT);
		energyIcon.setTopPad(26.0f);
		energyIcon.setRightPad(138.0f);
		energyIcon.autoPadding(26, 0, 0, 138);
		energyIcon.enableTilesetMode(baricons, 1, 0);
		UIList.add(energyIcon);
		
		btnA = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnA.autoPadding(0.0f, 0.0f, 5.0f, 90.0f);
		btnA.enableTextureMode(buttona);
		//btnA.setIntervalTime(Stopwatch.elapsedTimeMs());
		UIList.add(btnA);
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.enableTextureMode(buttonb);
		//btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		UIList.add(btnB);
		
		joypad = new UIJoypad(.45f, .45f, UIPosition.BOTTOMLEFT, player.getAngle(), joystickin);
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.enableTextureMode(joystickout);
		UIList.add(joypad);
				
		worldMinX = (-Tile.TILE_SIZE_F * (tileset[0].length / 2)) + (screenW / 2);
		worldMinY = (-Tile.TILE_SIZE_F * (tileset.length / 2)) + (screenH / 2);
		worldMaxX = (Tile.TILE_SIZE_F * (tileset[0].length / 2)) - (screenW / 2);
		worldMaxY = (Tile.TILE_SIZE_F * (tileset.length / 2)) - (screenH / 2);
		
		updateCameraPosition();
		updateRenderedEnts();
		updateRenderedTileset();
	}
	
	public void updateRenderedEnts()
	{
		//define current screen bounds
		final float minX, maxX, minY, maxY;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenW / 2);
		maxY = camPosY + (screenW / 2);
		
		for(Entity ent : entList)
		{
			//define max square bounds
			final float entMinX = ent.getXPos() - (float)ent.getDiagonal();
			final float entMaxX = ent.getXPos() + (float)ent.getDiagonal();
			final float entMinY = ent.getYPos() - (float)ent.getDiagonal();
			final float entMaxY = ent.getYPos() + (float)ent.getDiagonal();
			
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
		final float minX, maxX, minY, maxY, tilesetHalfWidth, tilesetHalfHeight;
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
		
		//make sure bounds don't exceed level edges
		if(tilesetMinX < 0)
			tilesetMinX = 0;
		if(tilesetMinY < 0)
			tilesetMinY = 0;
		if (tilesetMaxX > tileset[0].length - 1)
			tilesetMaxX = tileset[0].length - 1;
		if (tilesetMaxY > tileset.length - 1)
			tilesetMaxY = tileset.length - 1;
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
	
	public static Tile nearestTile(Entity ent, Tile[][] tileset)
	{	
		//\TODO Fix return null when offscreen
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
		if (!enemy.active)
			return;
		
		if (Vector2.subtract(enemy.getPos(), player.getPos()).magnitude() <  Enemy.OUTER_RADIUS)
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
		enemy.incrementLastTime((int)Stopwatch.getFrameTime());
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
					if (enemy.getLastTime() > enemy.getRandomTime())
					{
						Vector2 directionVec = new Vector2(enemy.getAngle());
						AttackBolt attack = new AttackBolt(Vector2.add(enemy.getPos(), directionVec), directionVec.scale(15), enemy.getAngle());
						attack.ignore(enemy);
						EntityManager.addEntity(attack);
						enemy.setRandomTime((int)(Math.random() * 500) + 500);
						enemy.setLastTime(0);
						attack.enableTilesetMode(Game.tilesetentities, 0, 3);
					}
					//move to player
					if (Vector2.subtract(enemy.getPos(), player.getPos()).magnitude() <  Enemy.INNER_RADIUS)
					{
						enemy.stop();
					}
					else
					{
						enemy.stop();
						enemy.rotateTo(Vector2.subtract(player.getPos(), enemy.getPos()).angleDeg());
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
					Vector2 nextNodePos = enemy.getPathToPlayer().getNode(enemy.getPlayerPathLocation() + 1).getPos();
					enemy.moveTo(nextNodePos);
					enemy.rotateTo(Vector2.subtract(nextNodePos, enemy.getPos()).angleDeg());
				}
			}
			else
			{
				runBecomeAgressiveAI(enemy);
			}
		}
		else if (enemy.getType() == AIType.TURRET)
		{
			float towardsPlayerAngle = (float)Vector2.subtract(player.getPos(), enemy.getPos()).angleDeg();
			if (enemy.getAngle() > towardsPlayerAngle + 5.0f || enemy.getAngle() < towardsPlayerAngle - 5.0f)
				enemy.rotateTo(towardsPlayerAngle);
			else
				enemy.setAngle(towardsPlayerAngle);
			
			if (enemy.getLastTime() > enemy.getRandomTime())
			{
				Vector2 directionVec = new Vector2(enemy.getAngle());
				final AttackBolt attack = new AttackBolt(Vector2.add(enemy.getPos(), directionVec), directionVec.scale(15), enemy.getAngle());
				attack.ignore(enemy);
				EntityManager.addEntity(attack);
				enemy.setRandomTime((int)(Math.random() * 500) + 500);
				enemy.setLastTime(0);
				attack.enableTilesetMode(Game.tilesetentities, 0, 3);
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
			float angleToPlayer = (float)Vector2.subtract(player.getPos(), enemy.getPos()).angleDeg();
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
				
				float angleToNode = (float)Vector2.subtract(nextNode.getPos(), enemy.getPos()).angleDeg();
				
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
				float angleToNode = (float)Vector2.subtract(closestNode.getPos(), enemy.getPos()).angleDeg();
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
		//triggerList.add(new Trigger(new CauseDoneScaling(player), new EffectEndGame(listener, false)));
		triggerList.add(new Trigger(new CausePlayerHealth(0, player), new EffectEndGame(listener, false)));
		
		for (Trigger t : triggerList)
		{
			if (t.getEffect() instanceof EffectEndGame)
				((EffectEndGame)t.getEffect()).setListener(listener);
		}
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
		final Vector2 startHVec = Vector2.subtract(startNode.getPos(), goalNode.getPos());
		startNode.setH(startHVec.magnitude());
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
						float newG = lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().magnitude();
						if (node.getG() > newG)
						{
							node.setG(newG);
							node.setF(node.getG() + node.getH());
							node.setParentNode(lowestF);
						}
					}
					else
					{
						final Vector2 hVec = Vector2.subtract(node.getPos(), goalNode.getPos());
						node.setG(lowestF.getG() + lowestF.getNodeLink(i).getNodeVec().magnitude());
						node.setH(hVec.magnitude());
						node.setF(node.getG() + node.getH());
						node.setParentNode(lowestF);
						openList.add(node);
						givenUp = false;
					}
				}
			}
			if (openList.isEmpty())
				break;
			else
			{
				lowestF = openList.get(0);
				for (int i = 1; i < openList.size(); i++)
				{
					if (openList.get(i).getF() < lowestF.getF())
						lowestF = openList.get(i);
				}
				openList.remove(lowestF);
				closedList.add(lowestF);
			}
			
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
	
	public boolean pathIsClear(final Vector2 startVec, final Vector2 endVec)
	{
		final Vector2 pathVec = Vector2.subtract(endVec, startVec).normalize();
		final Vector2 pathNormal = Vector2.getNormal(pathVec);
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
		if (player.userHasControl())
		{
			player.setAngle(joypad.getInputAngle());
			player.addPos(joypad.getInputVec().scale(Stopwatch.getFrameTime() * (player.getMoveSpeed() / 1000)));
		}
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
		
		//\TODO don't iterate through all and check if visible, have bounds available
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
