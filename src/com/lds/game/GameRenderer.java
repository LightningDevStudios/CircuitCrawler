package com.lds.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.lds.TextureLoader;

import android.opengl.GLU;

import android.content.Context;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	public boolean windowOutdated;
	int prevRenderCount, framescount;
	public static final float SPEED = 0.05f; //speed, in units per frame, of translation of Entities
	
	public GameRenderer (float screenW, float screenH, Context _context)
	{
		game = new Game(screenW, screenH);
		context = _context;
		windowOutdated = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//texture loading
		game.tl = new TextureLoader(gl, context);
		game.tl.load(R.drawable.tilesetcolors);
		game.tl.load(R.drawable.tilesetwire);
		
		game.initializeTileset();
		game.initializeSprites();
		//openGL settings
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//TODO move method somewhere cleaner, Game or TextureLoader
		
	}
	
	@Override
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
					//game.tileset[i][j].setTexture(tl.getTexture());
					game.tileset[i][j].draw(gl);
					gl.glLoadIdentity();
				}
			}
		}
		
		//Render all entities
		for (Entity ent : game.entList)
		{
			//checks for collision with all other entities in entList
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
			}
			
			moveInterpolate(ent);
			rotateInterpolate(ent);
			scaleInterpolate(ent);
			
			if (game.button1.isActive())
			{
				game.player2.scaleTo(4.0f, 1.0f);
			}
			else
			{
				game.player2.scaleTo(1.0f, 1.0f);
			}
			
			if (ent.isRendered)
			{
				/*System.out.println(ent.getClass().getName());
				if (ent.getClass().getName() == "com.lds.game.Sprite")
				{*/
					ent.renderNextFrame();
				//}
				renderedcount++;
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				gl.glRotatef(-ent.angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.xScl, ent.yScl, 1.0f);
				
				ent.draw(gl);
				gl.glLoadIdentity();
			}
			//TEMP, call onSufraceChanged each time, find new way through OpenGL...
			//this.onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
		}
		framescount++;
		
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
			ent.angle += SPEED * ent.interpAngle;
			
			//error check
			if (ent.angle <= ent.endAngle + SPEED / 2 && ent.angle >= ent.endAngle - SPEED / 2)
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
