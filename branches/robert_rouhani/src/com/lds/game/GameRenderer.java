package com.lds.game;

import java.util.ConcurrentModificationException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	int prevRenderCount;
	
	
	public GameRenderer (float screenW, float screenH)
	{
		game = new Game(screenW, screenH);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 0.41f, 0.71f, 0.5f);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		int renderedcount = 0;
		
		//Render all entities
		for (Entity ent : game.entList)
		{
			if (ent.isRendered)
			{
				renderedcount++;
				
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				gl.glLoadIdentity();
				
			}
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
		game.camPosX = xInput - (game.screenW / 2);
		game.camPosY = -yInput + (game.screenH / 2);
	}

}
