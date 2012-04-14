/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.ltdev.*;
import com.ltdev.cc.Tile.TileType;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.event.*;
import com.ltdev.cc.parser.Parser;
import com.ltdev.cc.physics.*;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.cc.trigger.*;
import com.ltdev.cc.ui.*;
import com.ltdev.cc.ui.Control.UIPosition;
import com.ltdev.graphics.Texture;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;
import com.ltdev.physics.*;
import com.ltdev.trigger.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

import org.xmlpull.v1.XmlPullParserException;

public class Game
{
    public static boolean worldOutdated, windowOutdated;
    
    public static float screenW, screenH;
	
    private Tileset tileset;
	public ArrayList<Entity> entities;
	public ArrayList<Control> uiList;
	private ArrayList<Trigger> triggerList;

	public World world;
	
	public ArrayList<Finger> fingerList;
	
	//Camera data
	public PointF camPos;
	
	private float worldMinX, worldMinY, worldMaxX, worldMaxY;
	
	//Testing data
	public UIButton btnB;	
	public UIJoypad joypad;
	public Player player;
	
	//Constructors
	public Game(Context context, GL11 gl, int levelId)
	{
	    camPos = new PointF();
	    
		TextureManager.addTexture("text", new Texture(R.raw.text, 256, 256, 16, 8, context, gl));
		TextureManager.addTexture("tilesetworld", new Texture(R.raw.tilesetworld, 512, 256, 16, 8, context, gl));
		TextureManager.addTexture("tilesetentities", new Texture(R.raw.tilesetentities, 256, 256, 8, 8, context, gl));
		TextureManager.addTexture("baricons", new Texture(R.raw.baricons, 32, 16, 2, 1, context, gl));
		
		TextureManager.addTexture("joystickout", new Texture(R.raw.joystickout, 64, 64, 1, 1, context, gl));
		TextureManager.addTexture("joystickin", new Texture(R.raw.joystickin, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("buttona", new Texture(R.raw.buttona, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("buttonb", new Texture(R.raw.buttonb, 32, 32, 1, 1, context, gl));
			
		entities = new ArrayList<Entity>();
		uiList = new ArrayList<Control>();
		triggerList = new ArrayList<Trigger>();
		fingerList = new ArrayList<Finger>();
		        		
		//StringRenderer sr = StringRenderer.getInstance();
		//sr.loadTextTileset(text);
				
		SoundPlayer.initialize(context);
		
		TextureManager.getTexture("tilesetworld").setMinFilter(GL11.GL_LINEAR, gl);
		TextureManager.getTexture("tilesetworld").setMagFilter(GL11.GL_LINEAR, gl);
		 		
		//Parser
		Parser parser = new Parser(context, levelId);
		
		try
		{
			parser.parseLevel(gl); 
		}
		
		//TODO cleanly exit the game on error.
		catch (XmlPullParserException e)
		{
		    Log.e("Circuit Crawler", "Error parsing level " + levelId);
		    return;
		}
		catch (IOException e)
		{
		    Log.e("Circuit Crawler", "Error reading level " + levelId);
		    return;
		}
	
		//retreive data from parser
		Tile[][] tileset = parser.tileset;
		
		entities.addAll(parser.entList);
		triggerList.addAll(parser.triggerList);
		
		this.tileset = new Tileset(tileset, TextureManager.getTexture("tilesetworld"));
		this.tileset.initialize(gl);
		
		player = parser.player;
		entities.add(player);
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.enableTextureMode(TextureManager.getTexture("buttonb"));
		//btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		uiList.add(btnB);
		
		joypad = new UIJoypad(.45f, .45f, UIPosition.BOTTOMLEFT, player.getAngle(), TextureManager.getTexture("joystickin"));
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.enableTextureMode(TextureManager.getTexture("joystickout"));
		uiList.add(joypad);
		
		//TODO camera class
		worldMinX = (-Tile.TILE_SIZE_F * (tileset[0].length / 2)) + (screenW / 2);
		worldMinY = (-Tile.TILE_SIZE_F * (tileset.length / 2)) + (screenH / 2);
		worldMaxX = (Tile.TILE_SIZE_F * (tileset[0].length / 2)) - (screenW / 2);
		worldMaxY = (Tile.TILE_SIZE_F * (tileset.length / 2)) - (screenH / 2);
		
		Vector2 worldSize = new Vector2(Tile.TILE_SIZE_F * tileset[0].length, Tile.TILE_SIZE_F * tileset.length);
		
		ArrayList<Shape> shapes = new ArrayList<Shape>();
        for (Entity ent : entities)
            shapes.add(ent.getShape());
		
        //TODO optimize into larger rectangles
		for (int i = 0; i < tileset.length; i++)
		{
		    for (int j = 0; j < tileset[i].length; j++)
		    {
		        if (tileset[i][j].getTileType() == TileType.WALL)
		        {
		            Vector2 position = new Vector2((j + 0.5f) * Tile.TILE_SIZE_F - 0.5f * worldSize.x(), -((i + 0.5f) * Tile.TILE_SIZE_F - 0.5f * worldSize.y()));
		            Rectangle r = new Rectangle(new Vector2(Tile.TILE_SIZE_F, Tile.TILE_SIZE_F), position, true);
		            r.setStatic(true);
		            shapes.add(r);
		        }
		    }
		}		
                
	    world = new World(worldSize, shapes);
	    
	    /*SpikeWall s = new SpikeWall(70, Vector2.add(player.getPos(), new Vector2(72 * 3 + 40, 0)), Direction.LEFT);
	    s.setTexture(TextureManager.getTexture("tilesetentities"));
	    EntityManager.addEntity(s);

	    Cannon c = new Cannon(40,  Vector2.add(player.getPos(), new Vector2(-72, 0)), 0, 20, 500000, player);
	    c.setTexture(TextureManager.getTexture("tilesetentities"));
	    EntityManager.addEntity(c);*/
	   
	    //LaserShooter LAZOR = new LaserShooter(Vector2.add(player.getPos(), new Vector2(-72, 0)), 40, 0, 20, 5, 1, player, world);
	    //LAZOR.setTexture(tilesetentities);
	    //EntityManager.addEntity(LAZOR); 
	    
		updateCameraPosition();
	}
	
	/**
	 * \todo legit AABB stuff.
	 * @return The entities that are actually visible.
	 */
	public ArrayList<Entity> getRenderedEnts()
	{
		//define current screen bounds
		final float minX, maxX, minY, maxY;
		minX = camPos.x - (screenW / 2);
		maxX = camPos.x + (screenW / 2);
		minY = camPos.y - (screenW / 2);
		maxY = camPos.y + (screenW / 2);
		
		ArrayList<Entity> renderList = new ArrayList<Entity>();
		for (Entity ent : entities)
		{
			AABB bbox = new AABB(ent.getShape().getWorldVertices());
			
			//values are opposite for entMin/Max because only the far tips have to be inside the screen (leftmost point on right border of screen)
			if (bbox.getLeftBound() <= maxX && bbox.getRightBound() >= minX && bbox.getBottomBound() <= maxY && bbox.getTopBound() >= minY)
				renderList.add(ent);
		}
		
		return renderList;
	}
	
	/**
	 * Updates the camera position.
	 */
	public void updateCameraPosition()
	{
		//move camera to follow player.
		camPos.x = player.getPos().x();
		camPos.y = player.getPos().y();
		
		
		//camera can't go further than defined level bounds
		if (camPos.x < worldMinX)
			camPos.x = worldMinX;
			
		else if (camPos.x > worldMaxX)
			camPos.x = worldMaxX;
		
		if (camPos.y < worldMinY)
			camPos.y = worldMinY;
		
		else if (camPos.y > worldMaxY)
			camPos.y = worldMaxY;
	}
	
	/**
	 * Finds the Tile nearest a specified entity.
	 * @param ent The entity to check.
	 * @param tileset The tileset that contains the entity.
	 * @return The nearest Tile.
	 */
	public static Tile nearestTile(Entity ent, Tile[][] tileset)
	{	
		//TODO Fix return null when offscreen
		final float tilesetHalfWidth = tileset[0].length * Tile.TILE_SIZE_F / 2;
		final float tilesetHalfHeight = tileset.length * Tile.TILE_SIZE_F / 2;
		
		final int x = (int)(ent.getPos().x() + tilesetHalfWidth) / Tile.TILE_SIZE;
		final int y = (int)(Math.abs(ent.getPos().y() - tilesetHalfHeight)) / Tile.TILE_SIZE;
		
		if (x < tileset[0].length && x >= 0 && y < tileset.length && y >= 0)
		{
			return tileset[y][x];
		}
		
		return null;
	}
	
	public void setGameOverEvent(GameOverListener listener)
	{
		//triggerList.add(new Trigger(new CauseDoneScaling(player), new EffectEndGame(listener, false)));
		
		for (Trigger t : triggerList)
		{
			if (t.getEffect() instanceof EffectEndGame)
				((EffectEndGame)t.getEffect()).setListener(listener);
		}
	}
	
	/**
	 * Updates all the fingers on the screen.
	 */
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

	/**
	 * Updates all the triggers in the level.
	 */
	public void updateTriggers()
	{
		for (Trigger t : triggerList)
			t.update();
	}
	
	/**
	 * \todo add real physics for player movement.
	 */
	public void updatePlayerPos()
	{
		//move player
		if (player.userHasControl())
		{
			player.setAngle((float)Math.toDegrees(joypad.getInputAngle()));
			//player.setPos(Vector2.add(player.getPos(), Vector2.scale(joypad.getInputVec(), /*Stopwatch.getFrameTime() **/ (1000 / 1000))));
			player.addImpulse(Vector2.scale(joypad.getInputVec(), player.getShape().getMass() * 5));
		}
		joypad.clearInputVec();
		
		//move heldObject if neccessary
		if (player.isHoldingObject())
			player.updateHeldObjectPosition();
		
		int indx = (int)((player.getPos().x() + tileset.getWidth() * Tile.TILE_SIZE_F / 2) / Tile.TILE_SIZE_F);
		int indy = (int)((-player.getPos().y() + tileset.getHeight() * Tile.TILE_SIZE_F /  2) / Tile.TILE_SIZE_F);
		
		Tile t = tileset.getTileAt(indx, indy);
		
		if (t != null)
		{
    		if (t.getTileType() == Tile.TileType.PIT)
    		    Player.kill();

    		else if (t.getTileType() == Tile.TileType.SlipperyTile)
    		    player.getShape().setKineticFriction(0);
    		
    		else
    		    player.getShape().setKineticFriction(5);
		}
	}
	
	/**
	 * Draws the tileset.
	 * @param gl The OpenGL context.
	 */
	public void renderTileset(GL11 gl)
	{
		tileset.draw(gl);
	}
	
	/**
	 * Checks whether the same instance of an object is contained in an ArrayList.
	 * @param entList The entity list.
	 * @param ent The entity to check for exact containment.
	 * @return A value indicating whether the instance "ent" is part of the entity list.
	 */
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
