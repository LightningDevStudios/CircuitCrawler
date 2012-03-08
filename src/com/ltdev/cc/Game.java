package com.ltdev.cc;

import android.content.Context;
import android.util.Log;

import com.ltdev.*;
import com.ltdev.cc.Tile.TileType;
import com.ltdev.cc.ai.Node;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.event.*;
import com.ltdev.cc.parser.Parser;
import com.ltdev.cc.physics.*;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.cc.trigger.*;
import com.ltdev.cc.ui.*;
import com.ltdev.cc.ui.Control.UIPosition;
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
    
    //Texture data
    //\TODO TextureManager class
    public static Texture tilesetwire;
    public static Texture text;
    public static Texture tilesetworld;
    public static Texture tilesetentities;
    public static Texture baricons;
	
    private Tileset tileset;
	public ArrayList<Entity> entities;
	public ArrayList<Control> UIList;
	private ArrayList<Trigger> triggerList;
	
	public EntityManager entManager;
	public World world;
	
	public ArrayList<Finger> fingerList;
	
	//Camera data
	public float camPosX;
	public float camPosY;
	
	private float worldMinX, worldMinY, worldMaxX, worldMaxY;
	
	//Testing data
	public UIButton btnB;	
	public UIJoypad joypad;
	public Player player;
	
	//Constructors
	public Game(Context context, GL11 gl, int levelId)
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
			
		entities = new ArrayList<Entity>();
		UIList = new ArrayList<Control>();
		triggerList = new ArrayList<Trigger>();
		fingerList = new ArrayList<Finger>();
		entManager = new EntityManager();
		        		
		//StringRenderer sr = StringRenderer.getInstance();
		//sr.loadTextTileset(text);
				
		SoundPlayer.initialize(context);
		
		tilesetworld.setMinFilter(GL11.GL_LINEAR);
		tilesetworld.setMagFilter(GL11.GL_LINEAR);
		
		TextureLoader.loadTexture(gl, tilesetwire);
		TextureLoader.loadTexture(gl, tilesetworld);
		TextureLoader.loadTexture(gl, tilesetentities);
		
		TextureLoader.loadTexture(gl, joystickout);
		TextureLoader.loadTexture(gl, joystickin);
		TextureLoader.loadTexture(gl, buttona);
		TextureLoader.loadTexture(gl, buttonb);
		TextureLoader.loadTexture(gl, baricons);
		TextureLoader.loadTexture(gl, energybarborder);
		TextureLoader.loadTexture(gl, healthbarborder);
		 		
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
		
		this.tileset = new Tileset(tileset, tilesetworld);
		this.tileset.initialize(gl);
		
		player = parser.player;
		entities.add(player);
		
		btnB = new UIButton(80.0f, 80.0f, UIPosition.BOTTOMRIGHT);
		btnB.autoPadding(0.0f, 0.0f, 90.0f, 5.0f);
		btnB.enableTextureMode(buttonb);
		//btnB.setIntervalTime(Stopwatch.elapsedTimeMs());
		UIList.add(btnB);
		
		joypad = new UIJoypad(.45f, .45f, UIPosition.BOTTOMLEFT, player.getAngle(), joystickin);
		joypad.autoPadding(0.0f, 5.0f, 5.0f, 0.0f);
		joypad.enableTextureMode(joystickout);
		UIList.add(joypad);
		
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
	    
	    SpikeWall s = new SpikeWall(70, Vector2.add(player.getPos(), new Vector2(72 * 3 + 40, 0)), Direction.LEFT);
	    s.setTexture(tilesetentities);
	    EntityManager.addEntity(s);

	    Cannon c = new Cannon(40,  Vector2.add(player.getPos(), new Vector2(-72, 0)), 0, 20, 500000, player);
	    c.setTexture(tilesetentities);
	    EntityManager.addEntity(c);
	   
	    //LaserShooter LAZOR = new LaserShooter(Vector2.add(player.getPos(), new Vector2(-72, 0)), 40, 0, 20, 5, 1, player, world);
	    //LAZOR.setTexture(tilesetentities);
	    //EntityManager.addEntity(LAZOR); 
	    
		updateCameraPosition();
	}
	
	/**
	 * \todo legit AABB stuff.
	 */
	public ArrayList<Entity> getRenderedEnts()
	{
		//define current screen bounds
		final float minX, maxX, minY, maxY;
		minX = camPosX - (screenW / 2);
		maxX = camPosX + (screenW / 2);
		minY = camPosY - (screenW / 2);
		maxY = camPosY + (screenW / 2);
		
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
	
	public void updateCameraPosition()
	{
		//move camera to follow player.
		camPosX = player.getPos().x();
		camPosY = player.getPos().y();
		
		
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
	 * rayfiring should not be here.
	 */
	public boolean pathIsClear(final Vector2 startVec, final Vector2 endVec)
	{
		/*final Vector2 pathVec = Vector2.subtract(endVec, startVec).normalize();
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
		}*/
		
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
	
	public void renderTileset(GL11 gl)
	{
		tileset.draw(gl);
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
