package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	int prevRenderCount, framescount;
	public static final float SPEED = 0.5f; //speed, in units per frame, of movement, rotation, and scaling of Entities
	
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
				
				//translation interpolation
				if ((ent.xPos != ent.endX || ent.yPos != ent.endY))
				{
					if (ent.posInterpMove)
					{
						ent.xPos += SPEED;
						ent.yPos += SPEED * ent.interpSlope;
					}
					else
					{
						ent.xPos -= SPEED;
						ent.yPos -= SPEED * ent.interpSlope;
					}
					
					if (ent.xPos <= ent.endX + SPEED && ent.xPos >= ent.endX - SPEED)
					{
						ent.xPos = ent.endX;
					}
					if (ent.yPos <= ent.endY + SPEED && ent.yPos >= ent.endY - SPEED)
					{
						ent.yPos = ent.endY;
					}
				}
				gl.glTranslatef(ent.xPos, ent.yPos, 0.0f);
				
				//rotation interpolation
				if (ent.angle != ent.endAngle)
				{
					if (ent.posInterpRotate)
					{
						ent.angle += SPEED / 2;
					}
					else
					{
						ent.angle -= SPEED / 2;
					}
					
					if (ent.angle <= ent.endAngle + SPEED / 2 && ent.angle >= ent.endAngle - SPEED / 2)
					{
						ent.angle = ent.endAngle;
					}
				}
				gl.glRotatef(-ent.angle, 0.0f, 0.0f, 1.0f);
				
				//scale interpolation
				if ((ent.xScl != ent.endXScl) || (ent.yScl != ent.endYScl))
				{
					if (ent.posInterpScl)
					{
						ent.xScl += SPEED / 10;
						ent.yScl += SPEED / 10 * ent.interpSclRatio;
					}
					else
					{
						ent.xScl -= SPEED / 10;
						ent.yScl -= SPEED / 10 * ent.interpSclRatio;
					}
					
					if (ent.xScl <= ent.endXScl + SPEED / 10 && ent.xScl >= ent.endXScl - SPEED / 10)
					{
						ent.xScl = ent.endXScl;
					}
					if (ent.yScl <= ent.endYScl + SPEED / 10 && ent.yScl >= ent.endYScl - SPEED / 10)
					{
						ent.yScl = ent.endYScl;
					}
				}
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
