package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	int prevRenderCount, framescount;
	public static final float SPEED = 0.01f; //speed, in units per frame, of translation of Entities
	
	public GameRenderer (float screenW, float screenH)
	{
		game = new Game(screenW, screenH);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 0.41f, 0.71f, 0.5f);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		int renderedcount = 0;

		//Render all entities
		for (Entity ent : game.entList)
		{
			moveInterpolate(gl, ent);
			rotateInterpolate(gl, ent);
			scaleInterpolate(gl, ent);
			
			if (ent.isRendered)
			{
				renderedcount++;
				
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(-ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				
				ent.draw(gl);
				gl.glLoadIdentity();
			}
			/*if (framescount >= 100 && framescount <= 1500)
			{
				game.camPosX -= 0.3f;
				game.camPosY -= 0.3f;
				game.updateLocalEntities();
			}*/
			framescount++;
			//TEMP, call onSufraceChanged each time, find new way through OpenGL...
			this.onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
		}
		
		//Update screen position and entities
		onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
		game.updateLocalEntities();
		
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
		if (xInput < 100 && yInput < 100)
		{
			game.player1.rotate(20.0f);
		}
		else
		{
			game.player1.moveTo(xInput - game.screenW / 2, -yInput + game.screenH / 2);
		}
	}
	
	/*************************
	 * Interpolation Methods *
	 *************************/
	
	//translation interpolation
	public void moveInterpolate (GL10 gl, Entity ent)
	{	
		//if the object needs to be interpolated
		if ((ent.xPos != ent.endX || ent.yPos != ent.endY))
		{
			//checks for collision with all other entities in entList
			for (Entity colEnt : game.entList)
			{
				if (colEnt != ent && ent.isColliding(colEnt))
				{
					ent.xPos -= SPEED * ent.interpX;
					ent.yPos -= SPEED * ent.interpY;
					ent.endX = ent.xPos;
					ent.endY = ent.yPos;
					return;
				}
			}
			
			//increments movement
			ent.xPos += SPEED * ent.interpX;
			ent.yPos += SPEED * ent.interpY;
			
			//error check
			if (ent.xPos <= ent.endX + SPEED / 2&& ent.xPos >= ent.endX - SPEED / 2)
			{
				ent.xPos = ent.endX;
			}
			if (ent.yPos <= ent.endY + SPEED / 2 && ent.yPos >= ent.endY - SPEED / 2)
			{
				ent.yPos = ent.endY;
			}
		}
	}
	
	//rotation interpolation
	public void rotateInterpolate (GL10 gl, Entity ent)
	{
		if (ent.angle != ent.endAngle)
		{
			//checks for collision with all other entities in entList
			for (Entity colEnt : game.entList)
			{
				if (colEnt != ent && ent.isColliding(colEnt))
				{
					ent.angle -= SPEED * ent.interpAngle;
					ent.endAngle = ent.angle;
					return;
				}
			}
			
			//increments angle
			ent.angle += SPEED * ent.interpAngle;
			
			//error check
			if (ent.angle <= ent.endAngle + SPEED / 2 && ent.angle >= ent.endAngle - SPEED / 2)
			{
				ent.angle = ent.endAngle;
			}
		}
	}
	
	//scale interpolation
	public void scaleInterpolate (GL10 gl, Entity ent)
	{
		if ((ent.xScl != ent.endXScl) || (ent.yScl != ent.endYScl))
		{
			//checks for collision with all other entities in entList
			for (Entity colEnt : game.entList)
			{
				if (colEnt != ent && ent.isColliding(colEnt))
				{
					ent.xScl -= SPEED * ent.interpXScl;
					ent.yScl -= SPEED * ent.interpYScl;
					ent.endXScl = ent.xScl;
					ent.endYScl = ent.yScl;
					return;
				}
			}
			
			//increments scaling
			ent.xScl += SPEED * ent.interpXScl;
			ent.yScl += SPEED * ent.interpYScl;
			
			//error check
			if (ent.xScl <= ent.endXScl + SPEED / 2 && ent.xScl >= ent.endXScl - SPEED / 2)
			{
				ent.xScl = ent.endXScl;
			}
			if (ent.yScl <= ent.endYScl + SPEED / 2 && ent.yScl >= ent.endYScl - SPEED / 2)
			{
				ent.yScl = ent.endYScl;
			}
		}
	}

}
