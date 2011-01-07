package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.view.MotionEvent;
import android.content.Context;

import com.lds.Graphics;
import com.lds.Stopwatch;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	public static Object syncObj;
	public boolean windowOutdated, testPB;
	public float tempSW, tempSH;
	public int btnATime, btnBTime;
	int i;
	int prevRenderCount;
	
	public GameRenderer (float screenW, float screenH, Context _context, Object syncObj)
	{
		tempSW = screenW;
		tempSH = screenH;
		context = _context;
		GameRenderer.syncObj = syncObj;
		windowOutdated = false;
		prevRenderCount = 2;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//openGL settings
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); //TODO change this later and make it

		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		//start the timer and use an initial tick to prevent errors where interpolation starts at -32768, making it go 
		Stopwatch.restartTimer();
		Stopwatch.tick();
		
		game = new Game(tempSW, tempSH, context, gl);
		testPB = true;
		btnATime = Stopwatch.elapsedTimeInMilliseconds();
		btnBTime = Stopwatch.elapsedTimeInMilliseconds();
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Stopwatch.tick();

		//testME = true;
		
		if (windowOutdated)
		{
			onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
			windowOutdated = false;
		}
		
		int renderedcount = 0;
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
					/*if (prevRenderCount < 2)
						game.tileset[i][j].renderMode = RenderMode.TILESET;*/
				}
			}
		}
		
		//Render all entities
		for (Entity ent : game.entList)
		{
			//scales up or down all PickupObj's
			ent.pickupScale();
			
			//Interpolation
			if (ent instanceof PhysEnt)
			{
				PhysEnt e = (PhysEnt)ent;
				e.moveInterpolate();
				e.rotateInterpolate();
				e.scaleInterpolate();
			}
			
			//checks for collision with all other entities in entList
			//TODO calculate only when necessary, not every frame.
			for (Entity colEnt : game.entList)
			{
				if (ent != colEnt)
				{
					if (ent.isColliding(colEnt))
					{
						ent.interact(colEnt);
						if (ent instanceof PhysEnt)
						{
							PhysEnt p = (PhysEnt)ent;
							p.stop();
						}
						if (colEnt instanceof PhysEnt)
						{
							PhysEnt p = (PhysEnt)ent;
							p.stop();
						}
					}
					else if (ent.colList.contains(colEnt))
					{
						ent.uninteract(colEnt);
						ent.colList.remove(colEnt);
					}
				}
				
				//checks for button interaction
				//TODO: Clean up, check for object in front of entity
				if (game.btnB.isPressed() && colEnt instanceof HoldObject)
				{
					HoldObject hObj = (HoldObject)colEnt;
					
					if (!game.player.isHoldingObject()) //not holding anything and is close enough
					{
						if (game.player.closeEnough(colEnt))
						{
							game.setHeldObjectPosition(hObj);
						}
					}
					else //holding object, button pressed
					{
						hObj.drop();
						game.player.dropObject();
					}
					game.btnB.unpress();
				}
			}
			
			if (ent instanceof HoldObject && ((HoldObject)ent).isHeld())
			{
				game.updateHeldObjectPosition((HoldObject)ent);
			}
			
			if (ent.isRendered)
			{
				if (ent instanceof Sprite)
				{
					Sprite spr = (Sprite)ent;
					spr.renderNextFrame();
				}
				
				renderedcount++;
				
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
			if (ent instanceof UIProgressBar)
			{
				UIProgressBar UIpb = (UIProgressBar)ent;
				if (UIpb.tempBool)
				{
					UIpb.setValue(UIpb.getValue() - 1);
					UIpb.updateGradient();
					UIpb.updateVertices();
					UIpb.autoPadding(UIpb.originalTopPad, UIpb.originalLeftPad, UIpb.originalBottomPad, UIpb.originalRightPad);
					UIpb.updatePosition(game.screenW, game.screenH);
					if (UIpb.getValue() == UIpb.getMinimum())
						UIpb.tempBool = false;
				}
				else
				{
					UIpb.setValue(UIpb.getValue() + 1);
					UIpb.updateGradient();
					UIpb.updateVertices();
					UIpb.autoPadding(UIpb.originalTopPad, UIpb.originalLeftPad, UIpb.originalBottomPad, UIpb.originalRightPad);
					UIpb.updatePosition(game.screenW, game.screenH);
					if (UIpb.getValue() == UIpb.getMaximum())
						UIpb.tempBool = true;
				}
			}
			gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
			ent.draw(gl);
			gl.glLoadIdentity();
			
		}
		
		viewWorld(gl);
		//Debugging info
		if (renderedcount != prevRenderCount)
		{
			System.out.println("Items rendered: " + renderedcount);
			prevRenderCount = renderedcount;
		}

		synchronized (syncObj)
		{
			syncObj.notify();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, game.camPosX - (float)(game.screenW/2), game.camPosX + (float)(game.screenW/2), game.camPosY - (float)(game.screenH/2), game.camPosY + (float)(game.screenH/2));
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		game.updateLocalTileset();
	}

	@Override
	public void onTouchInput(MotionEvent e) 
	{
		i++;
		System.out.println(i + ": " + e);
		float xInput = e.getRawX() - game.screenW / 2;
		float yInput = -e.getRawY() + game.screenH / 2;
		for (UIEntity ent : game.UIList)
		{
			if (xInput >= ent.xPos - ent.xSize / 2 && xInput <= ent.xPos + ent.xSize / 2 && yInput >= ent.yPos - ent.ySize / 2 && yInput <= ent.yPos + ent.ySize / 2)
			{
				if (ent instanceof UIJoypad)
				{
					UIJoypad UIjp = (UIJoypad)ent;
					
					float x = UIjp.getRelativeX(xInput);
					float y = UIjp.getRelativeY(yInput);
					
					/*game.camPosX += 0.05f * x;
					game.camPosY += 0.05f * y;*/
					
					double newRad = Math.atan2((double)y, (double)x);
					if (newRad < 0)
						newRad += 2 * Math.PI;
					
					float newAngle = (float)Math.toDegrees(newRad);
					game.player.setAngle(newAngle);
					game.player.setPos(game.player.xPos + (x / 10), game.player.yPos + (y / 10));
					game.camPosX = game.player.endX;
					game.camPosY = game.player.endY;
					windowOutdated = true;
				}
				if (ent instanceof UIButton)
				{
					if (Stopwatch.elapsedTimeInMilliseconds() - btnATime >= 100)
					{
						game.btnA.press();
						btnATime = Stopwatch.elapsedTimeInMilliseconds();
					}
					if (Stopwatch.elapsedTimeInMilliseconds() - btnBTime >= 100)
					{
						game.btnB.press();
						System.out.println("Beatin some meat!");
						btnBTime = Stopwatch.elapsedTimeInMilliseconds();
					}
				}
			}
		}
	}
	
	public void viewHUD(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, -game.screenW /2 , game.screenW / 2, -game.screenH / 2, game.screenH / 2);
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
