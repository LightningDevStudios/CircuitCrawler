package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums.RenderMode;

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
		prevRenderCount = 2;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//openGL settings
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glClearColor(0.39f, 0.58f, 0.93f, 0.5f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		game = new Game(tempSW, tempSH, context, gl);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (windowOutdated)
		{
			onSurfaceChanged(gl, (int)game.screenW, (int)game.screenH);
			windowOutdated = false;
		}
		
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
			
			if (ent instanceof PhysEnt)
			{
				PhysEnt e = (PhysEnt)ent;
				e.moveInterpolate();
				e.rotateInterpolate();
				e.scaleInterpolate();
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
				if (UIpb.value > 0) UIpb.value--;
				UIpb.updateGradient();
				UIpb.updateVertices();
				UIpb.autoPadding(5, 5, 0, 0);
				UIpb.updatePosition(game.screenW, game.screenH);
			}
			gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
			ent.draw(gl);
			gl.glLoadIdentity();
			
		}
		
		viewWorld(gl);
		
		//Update screen position and entities
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
		
		game.updateLocalTileset();
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
