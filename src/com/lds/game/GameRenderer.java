package com.lds.game;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.lds.EntityManager;
import com.lds.Finger;
import com.lds.Stopwatch;
import com.lds.UI.*;
import com.lds.Vibrator;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.physics.PhysicsManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	//public static boolean vibrateSetting = true;
	
	private Game game;
	private Context context;
	private Object syncObj;
	private GameInitializedListener gameInitializedListener;
	private PuzzleActivatedListener puzzleActivatedListener;
	private GameOverListener gameOverListener;
	private int levelId;
	private MediaPlayer mp;
	public boolean paused, charlieSheen;
	
	private Matrix4 projWorld, projUI;
	private PhysicsManager physMan;
	
	public GameRenderer(float screenW, float screenH, Context context, Object syncObj, int levelId)
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
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//openGL settings
	    gl.glViewport(0, 0, (int)Game.screenW, (int)Game.screenH);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		
		//start the timer and use an initial tick to prevent errors where elapsed time is a very large negative number
		Stopwatch.start();
		Stopwatch.tick();
		
		Entity.resetIndexBuffer();
		game = new Game(context, gl, levelId);
		
		//projWorld = Matrix4.ortho(game.camPosX - (Game.screenW / 2), game.camPosX + (Game.screenW / 2), game.camPosY + (Game.screenH / 2), game.camPosY - (Game.screenH / 2), 0, 1);
		projWorld = Matrix4.IDENTITY;
		projUI = Matrix4.ortho(-Game.screenW / 2 , Game.screenW / 2, Game.screenH / 2, -Game.screenH / 2, 0, 1);
		
		//Use VBOs if available
		Entity.genIndexBuffer(gl);
		
		if (Entity.useVBOs)
		{
			for (Entity ent : game.entList)
			{
				ent.genHardwareBuffers(gl);
			}
			
			for (Tile[] ta : game.tileset)
			{
				for (Tile t : ta)
				{
					t.genHardwareBuffers(gl);
				}
			}
			
			for (UIEntity ent : game.UIList)
			{
				ent.genHardwareBuffers(gl);
			}
		}
		
		if (gameInitializedListener != null)
			gameInitializedListener.onGameInitialized();
		
		for (Entity ent : game.entList)
		{
			if (ent instanceof PuzzleBox)
			{
				((PuzzleBox)ent).setPuzzleInitListener(puzzleActivatedListener);
			}
		}
	}
	
	public void onDrawFrame(GL10 gl) 
	{	
		if (paused)
			return;
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
		game.updateRenderedEnts();
		game.cleaner.update(game.entList, gl);
		game.updateFingers();
		game.renderTileset(gl);
		
		//update all entites
		for (Entity ent : game.entList)
		{
			ent.update();
			ent.updateGradientVBO(gl);
			ent.updateTextureVBO(gl);
		}
		
		/**********************************************
		 * Perform a Collision Check for all Entities *
		 **********************************************/
		
		//physMan.PerformCollisionCheck();
		
		//Iterates through all entities
		final int size = game.entList.size();
		for (int i = 0; i < size; i++)
		{
			
			final Entity ent = game.entList.get(i);
						
			//checks for collision with all other entities in entList if needed
			if (Game.worldOutdated)
			{
				//checks collision and interacts with all other Entities
				
				for (int j = i + 1; j < size; j++)
				{
					final Entity colEnt = game.entList.get(j);
					final boolean colListContains = Game.arrayListContains(ent.colList, colEnt) || Game.arrayListContains(colEnt.colList, ent);
					if (ent.isColliding(colEnt))
					{
						if (!colListContains)
						{
							ent.colList.add(colEnt);
							colEnt.colList.add(ent);
							ent.interact(colEnt);
							colEnt.interact(ent);
						}
					}
					else if (colListContains)
					{
						//System.out.println(ent.colList.size() + " " + colEnt.colList.size());
						ent.colList.remove(colEnt);
						colEnt.colList.remove(ent);
						if (ent.colList.isEmpty())
						{
							ent.uninteract(colEnt);
							colEnt.uninteract(ent);
						}
						//else if (colEnt.colList.isEmpty())
							//colEnt.uninteract(ent);
					}
				}
				
				if (ent instanceof PhysEnt)
				{
					final PhysEnt physEnt = (PhysEnt)ent;
					final Tile nearestTile = Game.nearestTile(physEnt, game.tileset);
					if (nearestTile == null)
						break;
					
					final int nearestTileX = nearestTile.xIndex;
					final int nearestTileY = nearestTile.yIndex;
					
					for (int j = nearestTileY - 1; j <= nearestTileY + 1; j++)
					{
						for (int k = nearestTileX - 1; k <= nearestTileX + 1; k++)
						{
							if (j < game.tileset.length && j >= 0 && k < game.tileset[0].length && k >= 0)
							{
								Tile tile = game.tileset[j][k];
								final boolean physColListContains = Game.arrayListContains(physEnt.colList, tile) || Game.arrayListContains(tile.colList, physEnt);
								if (tile.isColliding(physEnt))
								{
									if (!physColListContains)
									{
										physEnt.colList.add(tile);
										tile.colList.add(physEnt);
										physEnt.tileInteract(tile);
									}
								}
								else if (physColListContains)
								{
									physEnt.colList.remove(physEnt.colList.indexOf(tile));
									tile.colList.remove(physEnt);
									if (ent.colList.isEmpty())
										physEnt.tileUninteract(tile);
								}
							}
						}
					}
					
					//interacts with nearest tile to the entity; the tile it is standing on
					physEnt.onTileInteract(nearestTile);
					
					//bounces PhysEnts appropriately, excluding objects held by the player
					if (!game.player.isHoldingObject() || physEnt != game.player.getHeldObject())
						physEnt.addPos(physEnt.getBounceVec());
				}
				
				//moves the player correctly based on heldObject's bounceVecs
				if (ent == game.player && game.player.isHoldingObject())
				{
					game.player.addPos(game.player.getHeldObject().getBounceVec());
					game.player.updateHeldObjectPosition();
				}
			}
			
			/***************************
			 * Performs Button Actions *
			 ***************************/
	
			//inside of ent for loop
			//checks for whatever happens when B is pressed.
			if (game.btnB.isPressed())
			{
				if (ent instanceof HoldObject)
				{
					if (!game.player.isHoldingObject()) //not holding anything and is close enough
					{
						if (game.player.closeEnough(ent) && game.player.isFacing(ent))
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
					if (game.player.closeEnough(ent) && game.player.isFacing(ent))
					{
						((PuzzleBox)ent).run();
						Vibrator.vibrate(context, 100);
					}
					game.btnB.unpress();
				}
			}
		}
		
		
		//outside of ent for loop
		//causes button A to shoot when pressed
		if (game.btnA.isPressed())
		{
			if (!game.player.isHoldingObject())
			{
				if (game.player.getEnergy() != 0)
				{
					final Vector2 directionVec = new Vector2(game.player.getAngle());
					final AttackBolt attack = new AttackBolt(Vector2.add(game.player.getPos(), directionVec), directionVec.scale(25), game.player.getAngle());
					attack.ignore(game.player);
					attack.genHardwareBuffers(gl);
					EntityManager.addEntity(attack);
					game.player.loseEnergy(5);
					Vibrator.vibrate(context, 100);
					SoundPlayer.playSound(SoundPlayer.SHOOT_SOUND);
					attack.enableTilesetMode(Game.tilesetentities, 1, 3);
				}
			}
			else
			{
				game.player.throwObject();
				Vibrator.vibrate(context, 100);
			}
			
			game.btnA.unpress();
		}
		
		/**********************
		 * Render all Entites *
		 **********************/
					
		for (Entity ent : game.entList)
		{
			if (ent.isRendered())
			{								
				ent.draw(gl);
				gl.glLoadIdentity();
			}
		}
		
		game.btnB.unpress();
		
		//moved this out here so that all entities / colEnts can be compared, not just the next ones
		Game.worldOutdated = false;
		game.updateCameraPosition();
		
		//Render UI, in the UI perspective
		viewHUD(gl);
		
		for (UIEntity ent : game.UIList)
		{
			ent.update();
			ent.updateVertexVBO(gl);
			ent.updateGradientVBO(gl);
			ent.updateTextureVBO(gl);
			ent.draw(gl);
			gl.glLoadIdentity();
		}
		
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
	
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		updateCamPosition(gl);
	}

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
						final UIEntity ent = game.UIList.get(i);
						if (touchVec.getX() >= ent.getXPos() - ent.getXSize() / 2 && touchVec.getX() <= ent.getXPos() + ent.getXSize() / 2 && touchVec.getY() >= ent.getYPos() - ent.getYSize() / 2 && touchVec.getY() <= ent.getYPos() + ent.getYSize() / 2)
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
	//redraw the perspective
	public void updateCamPosition(GL10 gl)
	{
	    projWorld = Matrix4.ortho(game.camPosX - (Game.screenW / 2), game.camPosX + (Game.screenW / 2), game.camPosY + (Game.screenH / 2),  game.camPosY - (Game.screenH / 2), 0, 1);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(projWorld.array(), 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		game.updateRenderedTileset();
	}
	
	//draw a screen-based perspective, push the world perspective onto the OpenGL matrix stack
	public void viewHUD(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(projUI.array(), 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	//pop that perspective back from the stack
	public void viewWorld(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(projWorld.array(), 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void setGameOverEvent(GameOverListener listener) 
	{
		this.gameOverListener = listener;
		game.setGameOverEvent(listener);
	}
	
	public void setGameInitializedEvent(GameInitializedListener listener)
	{
		this.gameInitializedListener = listener;
	}

	public void setPuzzleActivatedEvent(PuzzleActivatedListener listener)
	{
		this.puzzleActivatedListener = listener;
	}
	
	public void onPuzzleWon() 
	{
		// \TODO Auto-generated method stub
	}

	public void onPuzzleFailed() 
	{
		// \TODO Auto-generated method stub
	}
	
	public void gameOver()
	{
		gameOverListener.onGameOver(charlieSheen);
	}
	
	public void clearTouchInput()
	{
		game.fingerList.clear();
	}
}
