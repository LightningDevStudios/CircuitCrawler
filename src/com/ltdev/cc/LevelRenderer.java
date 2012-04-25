/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.ltdev.Stopwatch;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.event.*;
import com.ltdev.cc.models.*;
import com.ltdev.math.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * The class that renders the game (and fires off all the other code, which is hax).
 * @author Lightning Development Studios
 */
public class LevelRenderer implements com.ltdev.LevelSurfaceView.Renderer
{
	//public static boolean vibrateSetting = true;
    private boolean paused, charlieSheen;
    
	private Game game;
	private Context context;
	private Object syncObj;
	private GameInitializedListener gameInitializedListener;
	private PuzzleActivatedListener puzzleActivatedListener;
	private GameOverListener gameOverListener;
	private int levelId;
	
	private float lightAngle, lightAngleSpeed;
	private Vector4 lightPos;
	
	private Matrix4 projWorld, projUI;
	
	/**
	 * Initializes a new instance of the GameRenderer class.
	 * @param screenW The width of the screen in pixels.
	 * @param screenH The height of the screen in pixels.
	 * @param context The active Android context.
	 * @param syncObj An object used to synchronize the rendering thread and the main thread.
	 * @param levelId The ID of the level to be played.
	 */
	public LevelRenderer(float screenW, float screenH, Context context, Object syncObj, int levelId)
	{
		Game.screenW = screenW;
		Game.screenH = screenH;
		this.context = context;
		this.syncObj = syncObj;
		Game.windowOutdated = false;
		Game.worldOutdated = false;
		this.levelId = levelId;
		SoundPlayer.initialize(context);
		paused = false;
		charlieSheen = false;
		lightPos = new Vector4(0, -100, 20, 1);
		lightAngle = 0;
		lightAngleSpeed = (float)Math.PI / 4;
	}

	/**
	 * Called when the OpenGL context is initialized and the surface is created.
	 * @param gl10 An OpenGL|ES 1.0 context.
	 * @param config The EGL configuration context.
	 */
	public void onSurfaceCreated(GL10 gl10, EGLConfig config)
	{
	    GL11 gl = (GL11)gl10;
	    
		//openGL settings
	    gl.glViewport(0, 0, (int)Game.screenW, (int)Game.screenH);
		gl.glShadeModel(GL11.GL_SMOOTH);
		gl.glEnable(GL11.GL_DEPTH_TEST);
		gl.glDepthFunc(GL11.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL11.GL_TEXTURE_2D);
        gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		gl.glClearColor(0, 0, 0, 1);
		
		//start the timer and use an initial tick to prevent errors where elapsed time is a very large negative number
		Stopwatch.start();
		Stopwatch.tick();

		game = new Game(context, gl, levelId);
		
		float aspectRatio = Game.screenW / Game.screenH;
		
		//projWorld = Matrix4.ortho(game.camPosX - (Game.screenW / 2), game.camPosX + (Game.screenW / 2), game.camPosY + (Game.screenH / 2), game.camPosY - (Game.screenH / 2), 0, 1);
		projWorld = Matrix4.perspective(-0.75f, 0.75f, 0.75f / aspectRatio, -0.75f / aspectRatio, 1f, Tile.SIZE_F * 8);
		projUI = Matrix4.ortho(-Game.screenW / 2 , Game.screenW / 2, Game.screenH / 2, -Game.screenH / 2, 0, 1);
		
		if (gameInitializedListener != null)
            gameInitializedListener.onGameInitialized();
	}
	
	/**
	 * Draws a frame.
	 * @param gl10 An OpenGL|ES 1.0 context.
	 */
	public void onDrawFrame(GL10 gl10)
	{	
		if (paused)
			return;

		GL11 gl = (GL11)gl10;
		
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//remove entities that are queued for removal
		//tick the stop watch every frame, gives relatively stable intervals
		//frameCount++;
		
		Stopwatch.tick();

		//Triggered when the perspective needs to be redrawn
		if (Game.windowOutdated)
		{		
			updateCamPosition(gl);
			Game.windowOutdated = false;
		}
		
		game.updatePlayerPos();
		
		game.updateTriggers();
		game.updateEntities(gl);
		game.integratePhysics();
		 
		/**********************
		 * Render all Entites *
		 **********************/
		
        gl.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        
        gl.glEnable(GL11.GL_LIGHTING);
        gl.glEnable(GL11.GL_LIGHT0);
		
        Vector2 angVec = Vector2.fromPolar(lightAngle, 200);
        //lightPos = new Vector4(angVec.x(), angVec.y(), 3, 1);
        //lightAngle += lightAngleSpeed * Stopwatch.getFrameTime() / 1000;
        //lightAngle %= (float)Math.PI * 2;
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos.array(), 0);
        //gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, new float[] { -50, -100f, 3, 1 }, 0);
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_AMBIENT, new float[] { 0.8f, 0.8f, 0.8f, 1f }, 0);
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, new float[] { 1f, 1f, 1f, 1f }, 0);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_CONSTANT_ATTENUATION, 0f);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_LINEAR_ATTENUATION, 1 / 8192f);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_QUADRATIC_ATTENUATION, 1 / 30000f);
        
        game.renderTileset(gl);
        
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        
		for (Entity ent : game.getRenderedEnts())
		{
		    gl.glPushMatrix();
			ent.draw(gl);
			gl.glPopMatrix();
		}

		//moved this out here so that all entities / colEnts can be compared, not just the next ones
		Game.worldOutdated = false;
		game.updateCameraPosition();
		
		viewWorld(gl);
				
		//poll for touch input
		synchronized (syncObj)
		{
			syncObj.notify();
		}
				
		//framerate count
		/*if (frameCount >= 10)
		{
			Log.d("LDS_Game", "FPS: " + (1000.0f / (Stopwatch.elapsedTimeMs() - game.frameInterval)));
			frameCount = 0;
		}*/
		
		if(game.isGameOver())
		{
		    gameOver();
		}
	}
	
	/**
	 * Called when the screen rotates or the screen otherwise changes.
	 * @param gl The OpenGL context.
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		updateCamPosition((GL11)gl);
	}

	/**
	 * Called when the user touches the screen.
	 * @param e The MotionEvent created by the touch event.
	 */
	public void onTouchInput(MotionEvent e)
	{
		game.handleTouchInput(e);
	}
	
	/**
	 * Update the projection matrix with camera position.
	 * @param gl The active OpenGL context.
	 * \todo make a Camera class, use a view matrix instead and keep the projection the same.
	 */
	public void updateCamPosition(GL11 gl)
	{
	    //projWorld = Matrix4.ortho(game.camPosX - (Game.screenW / 2), game.camPosX + (Game.screenW / 2), game.camPosY + (Game.screenH / 2),  game.camPosY - (Game.screenH / 2), 0, 1);
		viewWorld(gl);
		//game.updateRenderedTileset();
	}
	
	/**
	 * Load the world projection matrix.
	 * @param gl The active OpenGL context.
	 */
	public void viewWorld(GL11 gl)
	{
	    PointF camPos = game.getCameraPosition();
	    
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadMatrixf(projWorld.array(), 0);
		gl.glMatrixMode(GL11.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glMultMatrixf(Matrix4.translate(new Vector3(-camPos.x, -camPos.y, -Tile.SIZE_F * 4f)).array(), 0);
	}
	
	/**
	 * Gets the paused state of the renderer.
	 * @return A value indicating whether the game is paused.
	 */
	public boolean getPausedState()
	{
	    return paused;
	}
	
	/**
	 * Sets the game's paused state.
	 * @param state A value indicating whether the game is paused.
	 */
	public void setPausedState(boolean state)
	{
	    paused = state;
	}
	
	/**
	 * Sets the callback event for when the game ends.
	 * @param listener The callback.
	 */
	public void setGameOverEvent(GameOverListener listener) 
	{
		this.gameOverListener = listener;
		game.setGameOverEvent(listener);
	}
	
	/**
	 * Sets the callback event for when the game is done loading.
	 * @param listener The callback.
	 */
	public void setGameInitializedEvent(GameInitializedListener listener)
	{
		this.gameInitializedListener = listener;
	}

	/**
	 * Sets the callback event for when a puzzle is activated.
	 * @param listener The callback.
	 */
	public void setPuzzleActivatedEvent(PuzzleActivatedListener listener)
	{
		this.puzzleActivatedListener = listener;
	}
	
	/**
	 * Called when a puzzle is won.
	 */
	public void onPuzzleWon() 
	{
		// \TODO Auto-generated method stub
	}

	/**
	 * Called when a puzzle is lost.
	 */
	public void onPuzzleFailed() 
	{
		// \TODO Auto-generated method stub
	}
	
	/**
	 * Ends the game.
	 */
	public void gameOver()
	{
		gameOverListener.onGameOver(charlieSheen);
	}

	/**
	 * Called when the surface is destroyed.
	 */
    public void onUnload()
    {
        BallData.unload(null);
        BlockData.unload(null);
        ButtonDownData.unload(null);
        ButtonUpData.unload(null);
        DoorData.unload(null);
        SpikeWallData.unload(null);
    }
}
