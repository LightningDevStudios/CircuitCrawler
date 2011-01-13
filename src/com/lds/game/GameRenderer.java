package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.view.MotionEvent;
import android.content.Context;

import com.lds.Stopwatch;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	public Object syncObj;
	public boolean windowOutdated;
	public int frameInterval;
	
	public GameRenderer (float screenW, float screenH, Context context, Object syncObj)
	{
		Game.screenW = screenW;
		Game.screenH = screenH;
		this.context = context;
		this.syncObj = syncObj;
		windowOutdated = false;
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
		//clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		frameInterval = Stopwatch.elapsedTimeInMilliseconds();
		
		//tick the stopwatch every frame, gives relatively stable intervals
		Stopwatch.tick();
		
		//Triggered when the perspective needs to be redrawn
		if (windowOutdated)
		{
			updateCamPosition(gl);
			windowOutdated = false;
		}
		
		//remove entities that are queued for removal
		game.cleaner.clean(game.entList);
		
		//Update which entities are rendered
		game.updateLocalEntities();
				
		//Render tileset
		for (Tile[] ts : game.tileset)
		{
			for (Tile t : ts)
			{
				if (t.isRendered)
				{
					gl.glTranslatef(t.xPos, t.yPos, 0.0f);
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
			
			//checks for collision with all other entities in entList
			//TODO calculate only when necessary, not every frame.
			for (int j = i; j < game.entList.size(); j++)
			{
				Entity colEnt = game.entList.get(j);
				if (ent.isColliding(colEnt))
				{
					ent.interact(colEnt);
					colEnt.interact(ent);
					ent.colList.add(colEnt);
					colEnt.colList.add(ent);
				}
				else if (ent.colList.contains(colEnt))
				{
					ent.colList.remove(colEnt);
					colEnt.colList.remove(ent);
					
					//TODO is this right? fix for player + hObj on button, and un-interaction while hObj is still on the button
					if (ent.colList.isEmpty() && colEnt.colList.isEmpty())
					{
						ent.uninteract(colEnt);
						colEnt.uninteract(ent);
					}
				}
			}
	
			//checks for button interaction
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
				if(game.player.speed != 2)
				{
					game.player.setSpeed(2.0f);		
				}
				else
				{
					game.player.setSpeed(1.0f);
				}
				game.btnA.unpress();
			}
			
			//temp, checking nearestTile method
			game.nearestTile(game.player).updateTileset(3);
			
			//render it
			if (ent.isRendered)
			{								
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				
				gl.glLoadIdentity();
			}
		}
		
		//Render UI, in the UI perspective
		viewHUD(gl);
		
		for (UIEntity ent : game.UIList)
		{
			ent.update();
			
			gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
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
		System.out.println("FPS: " + (1000 / (Stopwatch.elapsedTimeInMilliseconds() - frameInterval)));
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
		//TODO remove this
		System.out.println("input");
		
		//get raw input
		float xInput = e.getRawX() - Game.screenW / 2;
		float yInput = -e.getRawY() + Game.screenH / 2;
		
		for (UIEntity ent : game.UIList)
		{
			//loop through UIList, see if input is within UIEntity bounds
			if (xInput >= ent.xPos - ent.xSize / 2 && xInput <= ent.xPos + ent.xSize / 2 && yInput >= ent.yPos - ent.ySize / 2 && yInput <= ent.yPos + ent.ySize / 2)
			{
				//Specific joypad code
				//TODO move to UIEntity generic method
				if (ent instanceof UIJoypad)
				{
					UIJoypad UIjp = (UIJoypad)ent;
					
					//get the relative X and Y coordinates
					float x = UIjp.getRelativeX(xInput);
					float y = UIjp.getRelativeY(yInput);
					
					//Figure out the angle
					double newRad = Math.atan2((double)y, (double)x);
					if (newRad < 0)
						newRad += 2 * Math.PI;
					float newAngle = (float)Math.toDegrees(newRad);
					float oldAngle = game.player.angle;
					
					//move the player
					game.player.setAngle(newAngle - 90.0f);
					game.player.setPos(game.player.xPos + (x / 10) * game.player.speed, game.player.yPos + (y / 10) * game.player.speed);
										
					//check collision and reverse motion if it's colliding with something solid
					for (Entity colEnt : game.entList)
					{
						
						if (colEnt != game.player && game.player.isColliding(colEnt) || (game.player.getHeldObject() != null && colEnt != game.player.getHeldObject() && game.player.getHeldObject().isColliding(colEnt)))
						{
							if (colEnt.willCollideWithPlayer())
							{
								game.player.setAngle(oldAngle);
								game.player.setPos(game.player.xPos - (x / 10) * game.player.speed, game.player.yPos - (y / 10) * game.player.speed);
								game.player.setShouldStop(false);
								
							}
						}
					}
					for (Tile[] ts : game.tileset)
					{
						for (Tile t: ts)
						{
							if (t.isRendered && (t.isColliding(game.player) || game.player.getHeldObject() != null && t.isColliding(game.player.getHeldObject())))
							{
								game.player.setAngle(oldAngle);
								game.player.setPos(game.player.xPos - (x / 10) * game.player.speed, game.player.yPos - (y / 10) * game.player.speed);
								game.player.setShouldStop(false);
							}
						}
					}
					
					//move the held object if one exists
					//TODO move to player?
					if (game.player.isHoldingObject())
						game.player.updateHeldObjectPosition();
					
					//move camera to follow player
					game.camPosX = game.player.endX;
					game.camPosY = game.player.endY;
					
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
						btn.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
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
