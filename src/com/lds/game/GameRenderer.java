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
	public static Object syncObj;
	public boolean windowOutdated, testPB;
	public int frameInterval;
	
	public GameRenderer (float screenW, float screenH, Context _context, Object syncObj)
	{
		Game.screenW = screenW;
		Game.screenH = screenH;
		context = _context;
		GameRenderer.syncObj = syncObj;
		windowOutdated = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//openGL settings
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); //TODO change this later and make it per-polygon

		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		//start the timer and use an initial tick to prevent errors where interpolation starts at -32768, making it go 
		Stopwatch.restartTimer();
		Stopwatch.tick();
		
		game = new Game(context, gl);
		testPB = true;		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		frameInterval = Stopwatch.elapsedTimeInMilliseconds();
		
		Stopwatch.tick();

		//testME = true;
		
		if (windowOutdated)
		{
			updateCamPosition(gl);
			windowOutdated = false;
		}
		
		game.cleaner.clean(game.entList);
		//Update screen position and entities
		game.updateLocalEntities();
				
		//Render tileset
		for (int i = 0; i < game.tileset.length; i++)
		{
			for (int j = 0; j < game.tileset[0].length; j++)
			{
				if (game.tileset[i][j].isRendered)
				{
					gl.glTranslatef(game.tileset[i][j].xPos, game.tileset[i][j].yPos, 0.0f);
					game.tileset[i][j].draw(gl);
					gl.glLoadIdentity();
				}
			}
		}
		
		//Render all entities
		for (Entity ent : game.entList)
		{
			
			//some button shit
			if (game.button.isActive())
			{
				game.door.open();
			}
			else
			{
				game.door.close();
			}
			
			ent.update();
			
			//checks for collision with all other entities in entList
			//TODO calculate only when necessary, not every frame.
			for (Entity colEnt : game.entList)
			{
				if (ent != colEnt)
				{
					if (ent.isColliding(colEnt))
					{
						ent.interact(colEnt);
					}
					else if (ent.colList.contains(colEnt))
					{
						ent.uninteract(colEnt);
						ent.colList.remove(colEnt);
					}
				}
				
				//checks for button interaction
				//TODO: check for object in front of entity
				if (game.btnB.isPressed() && colEnt instanceof HoldObject)
				{
					if (!game.player.isHoldingObject()) //not holding anything and is close enough
					{
						if (game.player.closeEnough(colEnt) && game.player.isFacing(colEnt))
						{
							game.player.holdObject((HoldObject)colEnt);
						}
					}
					else //holding object, button pressed
					{
						game.player.dropObject();
					}
					game.btnB.unpress();
				}
			}
	
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
			
			if (ent.isRendered)
			{								
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				gl.glLoadIdentity();
			}
		}
		
		viewHUD(gl);
		
		for (UIEntity ent : game.UIList)
		{
			ent.update();
			gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
			ent.draw(gl);
			gl.glLoadIdentity();
		}
		
		viewWorld(gl);
		
		//Debugging info
		
		//poll for touch input
		synchronized (syncObj)
		{
			syncObj.notify();
		}
		
		System.out.println("FPS: " + (1000 / (Stopwatch.elapsedTimeInMilliseconds() - frameInterval)));
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		updateCamPosition(gl);
	}

	@Override
	public void onTouchInput(MotionEvent e) 
	{
		float xInput = e.getRawX() - Game.screenW / 2;
		float yInput = -e.getRawY() + Game.screenH / 2;
		for (UIEntity ent : game.UIList)
		{
			if (xInput >= ent.xPos - ent.xSize / 2 && xInput <= ent.xPos + ent.xSize / 2 && yInput >= ent.yPos - ent.ySize / 2 && yInput <= ent.yPos + ent.ySize / 2)
			{
				if (ent instanceof UIJoypad)
				{
					UIJoypad UIjp = (UIJoypad)ent;
					
					float x = UIjp.getRelativeX(xInput);
					float y = UIjp.getRelativeY(yInput);
					
					double newRad = Math.atan2((double)y, (double)x);
					if (newRad < 0)
						newRad += 2 * Math.PI;
					float newAngle = (float)Math.toDegrees(newRad);
					float oldAngle = game.player.angle;
					game.player.setAngle(newAngle - 90.0f);
					game.player.setPos(game.player.xPos + (x / 10) * game.player.speed, game.player.yPos + (y / 10) * game.player.speed);
					for (Entity colEnt : game.entList)
					{
						
						if (colEnt != game.player && game.player.isColliding(colEnt))
						{
							if (colEnt.willCollideWithPlayer())
							{
								game.player.setAngle(oldAngle);
								game.player.setPos(game.player.xPos - (x / 10), game.player.yPos - (y / 10));
								game.player.setShouldStop(false); 
							}
						}
					}
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
				if (ent instanceof UIButton)
				{
					UIButton btn = (UIButton)ent;
					if (btn.canPress(500))
					{ 
						((UIButton)ent).press();
						btn.setIntervalTime(Stopwatch.elapsedTimeInMilliseconds());
					}
				}
			}
		}
	}
	
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
	
	public void viewWorld(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();
	}
}
