package com.lds.game;

import android.content.Context;

import com.lds.*;
import com.lds.Enums.*;
import com.lds.UI.*;
import com.lds.game.ai.Node;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.math.Vector2;
import com.lds.parser.Parser;
import com.lds.physics.CollisionDetector;
import com.lds.physics.World;
import com.lds.trigger.*;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

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
	public World world;
	public CollisionDetector CD;
	
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
	public Game(Context context, GL10 gl, int levelId)
	{		
		tilesetwire = new Texture(R.drawable.tilesetwire, 128, 128, 8, 8, context, "tilesetwire");
		text = new Texture(R.drawable.text, 256, 256, 16, 8, context, "text");
		tilesetworld = new Texture(R.raw.tilesetworld, 512, 256, 16, 8, context, "tilesetworld");
		tilesetentities = new Texture(R.raw.tilesetentities, 256, 256, 8, 8, context, "tilesetentities");
		baricons = new Texture(R.raw.baricons, 32, 16, 2, 1, context, "baricons");
		
		final Texture joystickout = new Texture(R.raw.joystickout, 64, 64, 1, 1, context, "joystickout");
		final Texture joystickin = new Texture(R.raw.joystickin, 32, 32, 1, 1, context, "joystickin");
		final Texture buttona = new Texture(R.raw.buttona, 32, 32, 1, 1, context, "buttona");
		final Texture buttonb = new Texture(R.raw.buttonb, 32, 32, 1, 1, context, "buttonb");
		final Texture energybarborder = new Texture(R.raw.energybarborder, 128, 16, 1, 1, context, "energybarborder");
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
		
		catch (Exception e) 
		{
		    e.printStackTrace();
		}
	
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
		
		UIImage healthIcon = new UIImage(16, 16, UIPosition.TOPRIGHT);
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
		
		UIImage energyIcon = new UIImage(16, 16, UIPosition.TOPRIGHT);
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
		
		for (Entity ent : entList)
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
		if (tilesetMinX < 0)
			tilesetMinX = 0;
		if (tilesetMinY < 0)
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
	
	public void setGameOverEvent(GameOverListener listener)
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
		if (player.userHasControl())
		{
			for (Finger f : fingerList)
			{
				f.update();
			}
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
