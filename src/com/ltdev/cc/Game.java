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
import android.view.MotionEvent;

import com.ltdev.*;
import com.ltdev.cc.Tile.TileType;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.event.*;
import com.ltdev.cc.parser.Parser;
import com.ltdev.cc.physics.*;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.cc.trigger.*;
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
	private ArrayList<Entity> entities;
	private ArrayList<Trigger> triggerList;

	private World world;
	
	//Camera data
	private PointF camPos;
	
	private float worldMinX, worldMinY, worldMaxX, worldMaxY;
	
	private Player player;
	
	private long touchInputTimer;
	private boolean pulling;
	private Vector2 joystick;
	
	//Constructors
	public Game(Context context, GL11 gl, int levelId)
	{	    
		TextureManager.addTexture("text", new Texture(R.raw.text, 256, 256, 16, 8, context, gl));
		TextureManager.addTexture("tilesetworld", new Texture(R.raw.tilesetworld, 512, 256, 16, 8, context, gl));
		TextureManager.addTexture("tilesetentities", new Texture(R.raw.tilesetentities, 256, 256, 8, 8, context, gl));
		TextureManager.addTexture("baricons", new Texture(R.raw.baricons, 32, 16, 2, 1, context, gl));
		TextureManager.addTexture("button", new Texture(R.raw.button, 128, 128, 1, 1, context, gl));
		TextureManager.addTexture("door", new Texture(R.raw.door, 128, 128, 1, 1, context, gl));
		TextureManager.addTexture("joystickout", new Texture(R.raw.joystickout, 64, 64, 1, 1, context, gl));
		TextureManager.addTexture("joystickin", new Texture(R.raw.joystickin, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("buttona", new Texture(R.raw.buttona, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("buttonb", new Texture(R.raw.buttonb, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("block", new Texture(R.raw.block, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("ball", new Texture(R.raw.ball, 32, 32, 1, 1, context, gl));
		TextureManager.addTexture("spikewall", new Texture(R.raw.spikewall, 8, 8, 1, 1, context, gl));
			
		setEntities(new ArrayList<Entity>());
		triggerList = new ArrayList<Trigger>();
		
		touchInputTimer = 0;
		joystick = Vector2.ZERO;
		//StringRenderer sr = StringRenderer.getInstance();
		//sr.loadTextTileset(text);
				
		SoundPlayer.initialize(context);
		
		//TextureManager.getTexture("tilesetworld").setMinFilter(GL11.GL_LINEAR, gl);
		//TextureManager.getTexture("tilesetworld").setMagFilter(GL11.GL_LINEAR, gl);
		 		
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
		
		this.player = parser.player;
		entities.add(player);
		
		camPos = new PointF(player.getPos().x(), player.getPos().y());
		
		//TODO camera class
		worldMinX = -Tile.SIZE_F * (float)tileset[0].length / 2f;
		worldMinY = -Tile.SIZE_F * (float)tileset.length / 2f;
		worldMaxX = Tile.SIZE_F * (float)tileset[0].length / 2f;
		worldMaxY = Tile.SIZE_F * (float)tileset.length / 2f;
		
		Vector2 worldSize = new Vector2(Tile.SIZE_F * tileset[0].length, Tile.SIZE_F * tileset.length);
		
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
		            Vector2 position = new Vector2((j + 0.5f) * Tile.SIZE_F - 0.5f * worldSize.x(), -((i + 0.5f) * Tile.SIZE_F - 0.5f * worldSize.y()));
		            Rectangle r = new Rectangle(new Vector2(Tile.SIZE_F, Tile.SIZE_F), position, true);
		            r.setStatic(true);
		            shapes.add(r);
		        }
		    }
		}		
                
	    setWorld(new World(worldSize, shapes));
	    
	    for (Entity ent : entities)
	    {
	        if (ent instanceof LaserShooter)
	            ((LaserShooter)ent).setWorld(world);
	    }
	    
	    /*SpikeWall s = new SpikeWall(70, Vector2.add(player.getPos(), new Vector2(72 * 3 + 40, 0)), Direction.LEFT);
	    s.setTexture(TextureManager.getTexture("tilesetentities"));
	    EntityManager.addEntity(s);*/

	    /*Cannon c = new Cannon(40,  Vector2.add(player.getPos(), new Vector2(-72, 0)), 0, 20, 500000, player);
	    c.setTexture(TextureManager.getTexture("tilesetentities"));
	    EntityManager.addEntity(c);
	   
	    //LaserShooter LAZOR = new LaserShooter(Vector2.add(player.getPos(), new Vector2(-72, 0)), 40, 0, 20, 5, 1, player, world);
	    //LAZOR.setTexture(tilesetentities);
	    //EntityManager.addEntity(LAZOR);  
	    
	    BreakableDoor door = new BreakableDoor(Vector2.add(player.getPos(), new Vector2(72, 0)), 3);
	    door.setTexture(TextureManager.getTexture("tilesetentities"));
	    EntityManager.addEntity(door); */
	    
		updateCameraPosition();
		
		for (Entity ent : entities)
        {
            ent.initialize(gl);
        }
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
		final float tilesetHalfWidth = tileset[0].length * Tile.SIZE_F / 2;
		final float tilesetHalfHeight = tileset.length * Tile.SIZE_F / 2;
		
		final int x = (int)(ent.getPos().x() + tilesetHalfWidth) / Tile.SIZE;
		final int y = (int)(Math.abs(ent.getPos().y() - tilesetHalfHeight)) / Tile.SIZE;
		
		if (x < tileset[0].length && x >= 0 && y < tileset.length && y >= 0)
		{
			return tileset[y][x];
		}
		
		return null;
	}
	
	/**
	 * Sets the game over event listener.
	 * @param listener The listener.
	 */
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
		    //player.setAngle((float)Math.toDegrees(joypad.getInputAngle()));
			//player.setPos(Vector2.add(player.getPos(), Vector2.scale(joypad.getInputVec(), /*Stopwatch.getFrameTime() **/ (1000 / 1000))));
		    //player.addImpulse(Vector2.scale(joypad.getInputVec(), player.getShape().getMass() * 5));
		}
		
		int indx = (int)((player.getPos().x() + tileset.getWidth() * Tile.SIZE_F / 2) / Tile.SIZE_F);
		int indy = (int)((-player.getPos().y() + tileset.getHeight() * Tile.SIZE_F /  2) / Tile.SIZE_F);
		
		Tile t = tileset.getTileAt(indx, indy);
		
		if (t != null)
		{
    		if (t.getTileType() == Tile.TileType.PIT)
    		    Player.kill();

    		else if (t.getTileType() == Tile.TileType.SLIP)
    		    player.getShape().setKineticFriction(0);
    		
    		else
    		    player.getShape().setKineticFriction(2);
		}
	}
	
	/**
	 * Updates the entity list and all the entities.
	 * @param gl The OpenGL context.
	 */
	public void updateEntities(GL11 gl)
	{
	    EntityManager.update(entities, world, gl);
	    
	    for (Entity ent : entities)
        {
            ent.update(gl);
        }
	}
	
	/**
	 * Integrate the physics world.
	 */
	public void integratePhysics()
	{
	    world.integrate(Stopwatch.getFrameTime());
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
	
	/**
	 * Sets the puzzle activated listener for all PuzzleBoxes in the level.
	 * @param listener The puzzle activated listener.
	 */
	public void setPuzzleActivatedListener(PuzzleActivatedListener listener)
	{
	    for (Entity ent : entities)
        {
            if (ent instanceof PuzzleBox)
            {
                ((PuzzleBox)ent).setPuzzleActivatedListener(listener);
            }
        }
	}
	
	/**
	 * Handle a touch input update.
	 * @param e The Android touch input event.
	 */
	public void handleTouchInput(MotionEvent e)
	{
	    if (player.userHasControl())
        {
            Game.worldOutdated = true;
            
            Vector2 impulse = Vector2.scale(new Vector2(e.getX() - screenW / 2 - joystick.x(), screenH / 2 - e.getY() - joystick.y()), 3.5f * Stopwatch.getFrameTime());
            if (player.getShape().getVelocity().length() < 200)
                player.addImpulse(impulse);
            player.setAngle(impulse.angleDeg());
            touchInputTimer += Stopwatch.getFrameTime();
            
            if (pulling)
                for (Entity ent : entities)
                    if (ent instanceof HoldObject && ((HoldObject)ent).isPullable() && CollisionDetector.radiusCheck(player.getShape(), ent.getShape(), 150))
                            ent.getShape().addImpulse(Vector2.scaleTo(Vector2.subtract(player.getPos(), ent.getPos()), 300f * Stopwatch.getFrameTime()));
            
            switch (e.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    joystick = new Vector2(e.getX() - screenW / 2, screenH / 2 - e.getY());
                    touchInputTimer = 0;
                    break;
                case MotionEvent.ACTION_UP:
                    if (touchInputTimer < 200)
                        for (Entity ent : entities)
                            if (ent instanceof HoldObject && CollisionDetector.radiusCheck(player.getShape(), ent.getShape(), 150))
                                    ent.getShape().addImpulse(Vector2.scaleTo(Vector2.subtract(player.getPos(), ent.getPos()), 1000f * Stopwatch.getFrameTime()));
                    touchInputTimer = 0;
                    joystick = Vector2.ZERO;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchInputTimer = 0;
                    pulling = true;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    pulling = false;
                    touchInputTimer = 0;
                    break;
                default:
                    break;
            }
        }
	}
	
	/**
	 * Gets the location of the camera.
	 * @return The camera's location.
	 */
	public PointF getCameraPosition()
	{
	    return camPos;
	}

    public World getWorld()
    {
        return world;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public ArrayList<Entity> getEntities()
    {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities)
    {
        this.entities = entities;
    }
}
