package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import android.content.Context;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	//I made game static, so that its entList could be accessed from lower classes, such as Entity. this allows us to easily remove objects from the list
	public Game game;
	public Context context;
	public boolean windowOutdated;
	public float tempSW, tempSH;
	int prevRenderCount;
	
	public GameRenderer (float screenW, float screenH, Context _context)
	{
		tempSW = screenW;
		tempSH = screenH;
		context = _context;
		windowOutdated = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		
		
		//openGL settings
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);

		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		game = new Game(tempSW, tempSH, context, gl);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		int renderedcount = 0;
		game.cleaner.clean(game.entList);
		
		//Render tileset
		for (int i = 0; i < game.tileset.length; i++)
		{
			for (int j = 0; j < game.tileset[0].length; j++)
			{
				if (game.tileset[i][j].isRendered)
				{
					gl.glTranslatef(game.tileset[i][j].xPos, game.tileset[i][j].yPos, 0.0f);
					gl.glRotatef(game.tileset[i][j].angle, 0.0f, 0.0f, 1.0f);
					gl.glScalef(game.tileset[i][j].xScl, game.tileset[i][j].yScl, 1.0f);
					game.tileset[i][j].draw(gl);
					gl.glLoadIdentity();
				}
			}
		}
		
		//Render all entities
		for (Entity ent : game.entList)
		{
			//scales up or down all PickupObj's
			//ent.pickupScale();
			
			//checks for collision with all other entities in entList
			/*for (Entity colEnt : game.entList)
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
			}*/
			
			moveInterpolate(ent);
			rotateInterpolate(ent);
			scaleInterpolate(ent);
			
			/*if (game.button1.isActive())
			{
				game.player2.scaleTo(4.0f, 1.0f);
			}
			else
			{
				game.player2.scaleTo(1.0f, 1.0f);
			}*/
			
			if (ent.isRendered)
			{
				ent.renderNextFrame();
				
				renderedcount++;
				
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(-ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				gl.glLoadIdentity();
			}
		}
		
		//Update screen position and entities
		game.updateLocalEntities();
		game.updateLocalTileset();
		if (windowOutdated)
		{
			onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
			windowOutdated = false;
		}
		//Debugging info
		if (renderedcount != prevRenderCount)
		{
			System.out.println("Items rendered: " + renderedcount);
			prevRenderCount = renderedcount;
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
	}

	@Override
	public void onTouchInput(float xInput, float yInput) 
	{
		/*if (xInput < 100 && yInput < 100)
		{
			game.player1.rotate(20.0f);
		}
		else
		{
			game.player1.moveTo(xInput - game.screenW / 2, -yInput + game.screenH / 2);
		}*/
		game.camPosX = xInput - (game.screenW / 2);
		game.camPosY = -yInput + (game.screenH / 2);
		windowOutdated = true;
	}
	
	/*************************
	 * Interpolation Methods *
	 *************************/
	
	//translation interpolation
	public void moveInterpolate (Entity ent)
	{	
		//if the object needs to be interpolated
		if ((ent.xPos != ent.endX || ent.yPos != ent.endY))
		{
			if (ent.shouldBreak)
			{
				ent.shouldBreak = false;
				return;
			}
			
			//increments movement
			ent.xPos += ent.speed * ent.interpX;
			ent.yPos += ent.speed * ent.interpY;
			
			//error check
			if (ent.xPos <= ent.endX + ent.speed / 2&& ent.xPos >= ent.endX - ent.speed / 2)
			{
				ent.xPos = ent.endX;
			}
			if (ent.yPos <= ent.endY + ent.speed / 2 && ent.yPos >= ent.endY - ent.speed / 2)
			{
				ent.yPos = ent.endY;
			}
		}
	}
	
	//rotation interpolation
	public void rotateInterpolate (Entity ent)
	{
		if (ent.angle != ent.endAngle)
		{
			if (ent.shouldBreak)
			{
				ent.shouldBreak = false;
				return;
			}
			
			//increments angle
			ent.angle += ent.speed * ent.interpAngle;
			
			//error check
			if (ent.angle <= ent.endAngle + ent.speed / 2 && ent.angle >= ent.endAngle - ent.speed / 2)
			{
				ent.angle = ent.endAngle;
			}
		}
	}
	
	//scale interpolation
	public void scaleInterpolate (Entity ent)
	{
		if ((ent.xScl != ent.endXScl) || (ent.yScl != ent.endYScl))
		{	
			if (ent.shouldBreak)
			{
				ent.shouldBreak = false;
				return;
			}
			
			//increments scaling
			ent.xScl += ent.speed * ent.interpXScl;
			ent.yScl += ent.speed * ent.interpYScl;
			
			//error check
			if (ent.xScl <= ent.endXScl + ent.speed / 2 && ent.xScl >= ent.endXScl - ent.speed / 2)
			{
				ent.xScl = ent.endXScl;
			}
			if (ent.yScl <= ent.endYScl + ent.speed / 2 && ent.yScl >= ent.endYScl - ent.speed / 2)
			{
				ent.yScl = ent.endYScl;
			}
		}
	}
}
