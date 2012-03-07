package com.ltdev.cc;

import android.content.Context;
import android.view.MotionEvent;

import com.ltdev.EntityManager;
import com.ltdev.Finger;
import com.ltdev.Stopwatch;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.event.*;
import com.ltdev.cc.physics.RaycastData;
import com.ltdev.cc.ui.*;
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
		lightPos = Vector4.scale(Vector4.UNIT_X, 100);
		lightAngle = 0;
		lightAngleSpeed = (float)Math.PI;
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
		//gl.glDepthRangef(0.01f, 1f);
		gl.glDepthFunc(GL11.GL_LEQUAL);
		gl.glDepthMask(true);
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);
		
		//start the timer and use an initial tick to prevent errors where elapsed time is a very large negative number
		Stopwatch.start();
		Stopwatch.tick();

		game = new Game(context, gl, levelId);
		
		float aspectRatio = Game.screenW / Game.screenH;
		
		//projWorld = Matrix4.ortho(game.camPosX - (Game.screenW / 2), game.camPosX + (Game.screenW / 2), game.camPosY + (Game.screenH / 2), game.camPosY - (Game.screenH / 2), 0, 1);
		projWorld = Matrix4.perspective(-2.5f, 2.5f, 2.5f / aspectRatio, -2.5f / aspectRatio, 0.01f, 1.5f);
		projUI = Matrix4.ortho(-Game.screenW / 2 , Game.screenW / 2, Game.screenH / 2, -Game.screenH / 2, 0, 1);
		
		for (Entity ent : game.entities)
		{
			ent.initialize(gl);
		}
		
		for (Control ent : game.UIList)
		{
			ent.genHardwareBuffers(gl);
		}
		
		if (gameInitializedListener != null)
			gameInitializedListener.onGameInitialized();
		
		for (Entity ent : game.entities)
		{
			if (ent instanceof PuzzleBox)
			{
				((PuzzleBox)ent).setPuzzleInitListener(puzzleActivatedListener);
			}
		}
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
			game.updatePlayerPos();
			updateCamPosition(gl);
			Game.windowOutdated = false;
		}
		
		game.updateTriggers();
		game.entManager.update(game.entities, gl);
		game.updateFingers();
		
		//update all entites
		for (Entity ent : game.entities)
		{
			ent.update(null);
		}
		
		//TODO: game.update(), chain off to world.update()
		game.world.integrate(Stopwatch.getFrameTime());
		
		//HACK: for the love of GOD move this out of LevelRenderer
		//Iterates through all entities
		final int size = game.entities.size();
		for (int i = 0; i < size; i++)
		{
			
			//final Entity ent = game.entities.get(i);
			
			/***************************
			 * Performs Button Actions *
			 ***************************/
	
			if (game.btnB.isPressed())
			{
			    RaycastData dat = game.world.rayCast(game.player.getPos(), game.player.getAngle());
			    
			    if (dat != null)
			    {
    			    Vector2 laserPos = Vector2.add(game.player.getPos(), new Vector2(dat.distance / 2 * (float)Math.cos(game.player.getAngle()), dat.distance / 2 * (float)Math.sin(game.player.getAngle())));
    			    Laser l = new Laser(10, dat.distance, game.player.getAngle(), laserPos);
    			    l.setTexture(Game.tilesetentities);
    			    EntityManager.addEntity(l);
			    }
			    //System.out.println("LOLZ");
			}
			
			//inside of ent for loop
			//checks for whatever happens when B is pressed.
			/*if (game.btnB.isPressed())
			{
				if (ent instanceof HoldObject)
				{
					if (!game.player.isHoldingObject()) //not holding anything and is close enough
					{
						if (CollisionDetector.radiusCheck(game.player.getShape(), ent.getShape()) && game.player.isFacing(ent))
						{
							game.player.holdObject((HoldObject)ent);
							Vibrator.vibrate(context, 100);
							game.btnB.unpress();
						}
					}
					else //holding object, button pressed
					{
						game.player.dropObject();
						Vibrator.vibrate(context, 100);
						game.btnB.unpress();
					}
				}
				else if (ent instanceof PuzzleBox)
				{
					if (CollisionDetector.radiusCheck(game.player.getShape(), ent.getShape()) && game.player.isFacing(ent))
					{
						((PuzzleBox)ent).run();
						Vibrator.vibrate(context, 100);
					}
					game.btnB.unpress();
				}
			}*/
		}
				
		/**********************
		 * Render all Entites *
		 **********************/
		
		gl.glEnable(GL11.GL_TEXTURE_2D);
		gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        
        gl.glEnable(GL11.GL_LIGHTING);
        gl.glEnable(GL11.GL_LIGHT0);
		
        Vector2 angVec = Vector2.fromPolar(lightAngle, 100);
        lightPos = new Vector4(angVec.x(), angVec.y(), 3, 1);
        lightAngle += lightAngleSpeed * Stopwatch.getFrameTime() / 1000;
        lightAngle %= (float)Math.PI * 2;
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos.array(), 0);
        //gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, new float[] { -50, -100f, 3, 1 }, 0);
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_AMBIENT, new float[] { 0.8f, 0.8f, 0.8f, 1f }, 0);
        gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, new float[] { 1f, 1f, 1f, 1f }, 0);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_CONSTANT_ATTENUATION, 0f);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_LINEAR_ATTENUATION, 1 / 8192f);
        gl.glLightf(GL11.GL_LIGHT0, GL11.GL_QUADRATIC_ATTENUATION, 1 / 20000f);
        
        game.renderTileset(gl);
        
        
        gl.glDisable(GL11.GL_LIGHT0);
        gl.glDisable(GL11.GL_LIGHTING);
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        
		for (Entity ent : game.getRenderedEnts())
		{
		    gl.glPushMatrix();
			ent.draw(gl);
			gl.glPopMatrix();
		}
		
		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        
		
		game.btnB.unpress();
		
		//moved this out here so that all entities / colEnts can be compared, not just the next ones
		Game.worldOutdated = false;
		game.updateCameraPosition();
		
		//Render UI, in the UI perspective
		viewHUD(gl);
		
		gl.glDisable(GL11.GL_DEPTH_TEST);
		gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		
		for (Control ent : game.UIList)
		{
		    gl.glPushMatrix();
			ent.update();
			ent.updateVertexVBO(gl);
			ent.updateGradientVBO(gl);
			ent.updateTextureVBO(gl);
			ent.draw(gl);
			gl.glPopMatrix();
		}
		
		gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		gl.glEnable(GL11.GL_DEPTH_TEST);
		
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
		if (game.player.userHasControl())
		{
			Game.worldOutdated = true;
			for (int i = 0; i < game.fingerList.size(); i++)
			{
				final Finger f = game.fingerList.get(i);
				final Vector2 touchInput = new Vector2(e.getX(f.getPointerId()) - Game.screenW / 2, Game.screenH / 2 - e.getY(f.getPointerId()));
				f.setPosition(touchInput);
			}
			
			switch (e.getAction() & MotionEvent.ACTION_MASK)
			{
				case MotionEvent.ACTION_POINTER_DOWN:
				case MotionEvent.ACTION_DOWN:
					final int fingerIndex = e.getPointerId(e.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT);
					final Vector2 touchVec = new Vector2(e.getX(e.getPointerCount() - 1) - Game.screenW / 2, Game.screenH / 2 - e.getY(e.getPointerCount() - 1));
					boolean onEnt = false;
					for (int i = 0; i < game.UIList.size(); i++)
					{
						final Control ent = game.UIList.get(i);
						if (touchVec.x() >= ent.getPos().x() - ent.getSize().x() / 2 && touchVec.x() <= ent.getPos().x() + ent.getSize().x() / 2 && touchVec.y() >= ent.getPos().y() - ent.getSize().y() / 2 && touchVec.y() <= ent.getPos().y() + ent.getSize().y() / 2)
						{
							final Finger newFinger = new Finger(touchVec, ent, e.getPointerId(fingerIndex));
							newFinger.onStackPush();
							game.fingerList.add(newFinger);
							onEnt = true;
							break;
						}
					}
					if (!onEnt)
					{
						final Finger newFinger = new Finger(null, null, e.getPointerId(fingerIndex));
						game.fingerList.add(newFinger);
					}
					
					break;
				case MotionEvent.ACTION_UP:
					for (final Finger f : game.fingerList)
					{
						f.onStackPop();
					}
					game.fingerList.clear();
					break;
				case MotionEvent.ACTION_POINTER_UP:
					if (!game.fingerList.isEmpty())
					{
						final int fIndex = e.getPointerId(e.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT);
						for (int i = 0; i < game.fingerList.size(); i++)
						{
							final Finger f = game.fingerList.get(i);
							if (fIndex == f.getPointerId())
								game.fingerList.remove(i).onStackPop();
						}
					}
					break;
            default:
                break;
			}
		}
		
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
	 * Loads the UI projection matrix.
	 * @param gl The active OpenGL context.
	 */
	public void viewHUD(GL11 gl)
	{
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadMatrixf(projUI.array(), 0);
		gl.glMatrixMode(GL11.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * Load the world projection matrix.
	 * @param gl The active OpenGL context.
	 */
	public void viewWorld(GL11 gl)
	{
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadMatrixf(projWorld.array(), 0);
		gl.glMatrixMode(GL11.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glMultMatrixf(Matrix4.translate(new Vector3(-game.camPosX, -game.camPosY, -1)).array(), 0);
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
	 * Remove all Fingers currently touching the screen.
	 */
	public void clearTouchInput()
	{
		game.fingerList.clear();
	}
}
