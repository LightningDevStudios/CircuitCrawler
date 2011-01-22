package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Debug;
import android.view.MotionEvent;
import android.content.Context;

import com.lds.EntityCleaner;
import com.lds.Stopwatch;
import com.lds.Vector2f;
import com.lds.game.entity.*;
import com.lds.trigger.*;
import com.lds.UI.*;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	public Object syncObj;
	public boolean windowOutdated, testDebug = true;
	public int frameInterval, frameCount = 0;
	
	public GameRenderer (float screenW, float screenH, Context context, Object syncObj)
	{
		Game.screenW = screenW;
		Game.screenH = screenH;
		this.context = context;
		this.syncObj = syncObj;
		windowOutdated = false;
		Game.worldOutdated = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		
		//openGL settings
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); //TODO change this later and make it per-poly?

		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		//start the timer and use an initial tick to prevent errors where elapsed time is a very large negative number
		Stopwatch.restartTimer();
		Stopwatch.tick();
		
		game = new Game(context, gl);		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		frameCount++;
		if (frameCount == 100)
			Debug.startMethodTracing("LDS_Game4");
				
		if(!game.entList.contains(game.player))
		{
			game = null;
			game = new Game(context, gl);
			game.updateLocalEntities();
			game.updateLocalTileset();
		}
		
		//clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		frameInterval = Stopwatch.elapsedTimeMs();
		
		//tick the stopwatch every frame, gives relatively stable intervals
		Stopwatch.tick();
				
		//iterate through triggers
		for (Trigger t : game.triggerList)
		{
			t.update();
		}
		
		//remove entities that are queued for removal
		game.cleaner.clean(game.entList);
				
		//Triggered when the perspective needs to be redrawn
		if (windowOutdated)
		{
			updateCamPosition(gl);
			windowOutdated = false;
		}
				
		//Update which entities are rendered
		game.updateLocalEntities();
				
		//Render tileset
		for (Tile[] ts : game.tileset)
		{
			for (Tile t : ts)
			{
				if (t.isRendered())
				{
					gl.glTranslatef(t.getXPos(), t.getYPos(), 0.0f);
					t.draw(gl);
					
					gl.glLoadIdentity();
				}
			}
		}
				
		//Render all entities
		for (int i = 0; i < game.entList.size(); i++)
		{
			Entity ent = game.entList.get(i);
			ent.update();
						
			//checks for collision with all other entities in entList if needed
			if (Game.worldOutdated)
			{
				for (int j = i + 1; j < game.entList.size(); j++)
				{
					Entity colEnt = game.entList.get(j);
					if (ent.isColliding(colEnt))
					{
						if (!ent.colList.contains(colEnt))
						{
							ent.colList.add(colEnt);
							colEnt.colList.add(ent);
							ent.interact(colEnt);
							colEnt.interact(ent);
						}
					}
					else if (ent.colList.contains(colEnt))
					{
						ent.colList.remove(colEnt);
						colEnt.colList.remove(ent);
						System.out.println(ent.colList.size() + " " + colEnt.colList.size());
						//TODO may or may not work.
						if (ent.colList.isEmpty())
						{
							ent.uninteract(colEnt);
							colEnt.uninteract(ent);
						}
					}
				}
			}
	
			//checks for whatever happens when B is pressed
			if (game.btnB.isPressed() && ent instanceof HoldObject)
			{
				if (!game.player.isHoldingObject()) //not holding anything and is close enough
				{
					if (game.player.closeEnough(ent) && game.player.isFacing(ent))
					{
						game.player.holdObject((HoldObject)ent);
					}
				}
				else //holding object, button pressed
				{
					game.player.dropObject();
				}
				game.btnB.unpress();
			}
			
			//set btnA to speed up the player when pressed
			if (game.btnA.isPressed())
			{		
				if(game.player.getSpeed() != 2)
				{
					game.player.setSpeed(2.0f);		
				}
				else
				{
					game.player.setSpeed(1.0f);
				}
				game.btnA.unpress();
			}
						
			//render it
			if (ent.isRendered())
			{								
				gl.glTranslatef(ent.getXPos(), ent.getYPos(), 0.0f);
				gl.glRotatef(ent.getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.getXScl(), ent.getYScl(), 1.0f);
				ent.draw(gl);
				
				gl.glLoadIdentity();
			}
		}
		
		//moved this out here so that all entities / colEnts can be compared, not just the next ones
		Game.worldOutdated = false;
		
		//Render UI, in the UI perspective
		viewHUD(gl);
		
		for (UIEntity ent : game.UIList)
		{
			ent.update();
			
			gl.glTranslatef(ent.getXPos(), ent.getYPos(), 0.0f);
			ent.draw(gl);
			
			gl.glLoadIdentity();
		}
		
		viewWorld(gl);
				
		//poll for touch input
		synchronized (syncObj)
		{
			syncObj.notify();
		}
		
		//framerate count
		System.out.println("FPS: " + (1000 / (Stopwatch.elapsedTimeMs() - frameInterval)));
		
		if (frameCount == 101)
		{
			testDebug = false;
			Debug.stopMethodTracing();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		updateCamPosition(gl);
	}

	@Override
	//TODO move heldObj back when it collides with something
	public void onTouchInput(MotionEvent e) 
	{
		//get raw input
		float xInput = e.getRawX() - Game.screenW / 2;
		float yInput = -e.getRawY() + Game.screenH / 2;
		
		for (UIEntity ent : game.UIList)
		{
			//loop through UIList, see if input is within UIEntity bounds
			if (xInput >= ent.getXPos() - ent.getXSize() / 2 && xInput <= ent.getXPos() + ent.getXSize() / 2 && yInput >= ent.getYPos() - ent.getYSize() / 2 && yInput <= ent.getYPos() + ent.getYSize() / 2)
			{
				//Specific joypad code
				//TODO move to UIEntity generic method
				if (ent instanceof UIJoypad && game.player.userHasControl())
				{
					UIJoypad UIjp = (UIJoypad)ent;
					
					Tile test = game.nearestTile(game.player);
					
					if (test != null && test.isPit())
					{
						game.textbox.reloadTexture("Yer a wizerd harry!");
						game.player.disableUserControl();
						game.player.scaleTo(0, 0);
						game.player.moveTo(test.getXPos(), test.getYPos());
					}
					
					//get the relative X and Y coordinates
					Vector2f tempMoveVec = UIjp.getMovementVec((int)xInput, (int)yInput);
					
					//Figure out the angle
					float newAngle = (float)Math.toDegrees(tempMoveVec.angle());
					float oldAngle = game.player.getAngle();
					
					//move the player
					//TODO move w/ time, use move()?
					game.player.setAngle(newAngle + 90.0f);
					Vector2f moveVec = Vector2f.scale(tempMoveVec, game.player.getSpeed() / 10);
					game.player.setPos(Vector2f.add(game.player.getPos(), moveVec));
					Game.worldOutdated = true;
					
					//check collision and reverse motion if it's colliding with something solid
					for (Entity colEnt : game.entList)
					{
						
						if (colEnt != game.player && game.player.isColliding(colEnt) || (game.player.getHeldObject() != null && colEnt != game.player.getHeldObject() && game.player.getHeldObject().isColliding(colEnt)))
						{
							if (colEnt.willCollideWithPlayer())
							{
								game.player.setAngle(oldAngle);
								game.player.setPos(Vector2f.add(game.player.getPos(), Vector2f.getNormal(moveVec)));
								Game.worldOutdated = false;
							}
						}
					}
					for (Tile[] ts : game.tileset)
					{
						for (Tile t: ts)
						{
							if (t.isRendered() && (t.isColliding(game.player) || game.player.getHeldObject() != null && t.isColliding(game.player.getHeldObject())))
							{
								game.player.setAngle(oldAngle);
								game.player.setPos(Vector2f.add(game.player.getPos(), Vector2f.getNormal(moveVec)));
								Game.worldOutdated = false;
							}
						}
					}
					
					//move the held object if one exists
					//TODO move to player?
					if (game.player.isHoldingObject())
						game.player.updateHeldObjectPosition();
					
					//Tile nTile = game.nearestTile(game.player);
					
					/*if (nTile.isPit())
					{
						game.player.moveTo(nTile.getXPos(), nTile.getYPos());
						game.player.scaleTo(0.0f, 0.0f);
					}*/
					
					//move camera to follow player
					game.camPosX = game.player.getXPos();
					game.camPosY = game.player.getYPos();
					
					//camera can't go further than defined level bounds
					if (game.camPosX < game.worldMinX)
						game.camPosX = game.worldMinX;
						
					else if (game.camPosX > game.worldMaxX)
						game.camPosX = game.worldMaxX;
					
					if (game.camPosY < game.worldMinY)
						game.camPosY = game.worldMinY;
					
					else if (game.camPosY > game.worldMaxY)
						game.camPosY = game.worldMaxY;
					
					windowOutdated = true;					
				}
				
				//UIButton specific code
				if (ent instanceof UIButton)
				{
					UIButton btn = (UIButton)ent;
					
					//500ms delay between presses
					if (btn.canPress(500))
					{ 
						((UIButton)ent).press();
						btn.setIntervalTime(Stopwatch.elapsedTimeMs());
					}
				}
			}
		}
	}
	
	//redraw the perspective
	public void updateCamPosition(GL10 gl)
	{
		gl.glViewport(0, 0, (int)Game.screenW, (int)Game.screenH);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, game.camPosX - (Game.screenW/2), game.camPosX + (Game.screenW/2), game.camPosY - (Game.screenH/2), game.camPosY + (Game.screenH/2));
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		game.updateLocalTileset();
	}
	
	//draw a screen-based perspective, push the world perspective onto the OpenGL matrix stack
	public void viewHUD(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, -Game.screenW /2 , Game.screenW / 2, -Game.screenH / 2, Game.screenH / 2);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
	}
	
	//pop that perspective back from the stack
	public void viewWorld(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();
	}
}
