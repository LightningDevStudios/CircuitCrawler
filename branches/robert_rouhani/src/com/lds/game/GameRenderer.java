package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class GameRenderer implements Renderer
{
	Game game;
	int framescount;
	
	
	public GameRenderer (float screenW, float screenH)
	{
		game = new Game(screenW, screenH);
		framescount = 0;
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
		for (Entity ent : game.entList)
		{
			if (ent.isRendered)
			{
				renderedcount++;
				gl.glLoadIdentity();
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				
			}
			if (framescount >= 100 && framescount <= 1500)
			{
				game.camPosX -= 0.3f;
				game.camPosY -= 0.3f;
				game.updateLocalEntities();
			}	
			framescount++;
			
			//TEMP, call onSufraceChanged each time, find new way through OpenGL...
			this.onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
		}
		System.out.println("Items rendered: " + renderedcount);
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

}
