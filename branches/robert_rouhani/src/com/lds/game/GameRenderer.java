package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;

import android.content.Context;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	int prevRenderCount;
	public boolean windowOutdated;
	
	
	public GameRenderer (float screenW, float screenH, Context _context)
	{
		game = new Game(screenW, screenH);
		this.context = _context;
		windowOutdated = false;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		game.tileset[3][4].loadTexture(gl, context);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//gl.glHint(GL10.GL_TEXTURE_C,GL10.GL_FASTEST);
		//TODO move method somewhere cleaner, Game or TextureLoader
		
	}

	//TODO move interpolation to another method, less clutter in main loop
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		int renderedcount = 0;
		
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
			if (ent.isRendered)
			{
				renderedcount++;
				//ent.loadTexture(gl, this.context);
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				ent.draw(gl);
				gl.glLoadIdentity();
				
			}
		}
		
		//Update screen position and entities
		
		game.updateLocalEntities();
		if (windowOutdated)
		{
			onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
		}
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
		windowOutdated = true;
	}

}
