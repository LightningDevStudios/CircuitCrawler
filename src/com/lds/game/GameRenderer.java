package com.lds.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Debug;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.content.Context;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.Vector2f;
import com.lds.game.entity.*;
import com.lds.game.event.*;
import com.lds.trigger.*;
import com.lds.UI.*;

public class GameRenderer implements com.lds.Graphics.Renderer
{
	public Game game;
	public Context context;
	public Object syncObj;
	public boolean windowOutdated, gameOver;
	public int playerMoveTimeMs, frameInterval, frameCount = 0;	
	public OnGameInitializedListener gameInitializedListener;
	public OnPuzzleActivatedListener puzzleActivatedListener;
	public OnGameOverListener gameOverListener;
	
	public GameRenderer (float screenW, float screenH, Context context, Object syncObj)
	{
		Game.screenW = screenW;
		Game.screenH = screenH;
		this.context = context;
		this.syncObj = syncObj;
		windowOutdated = false;
		Game.worldOutdated = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//openGL settings
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); //TODO change this later and make it per-poly?

		gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		
		//start the timer and use an initial tick to prevent errors where elapsed time is a very large negative number
		Stopwatch.restartTimer();
		Stopwatch.tick();
		playerMoveTimeMs = Stopwatch.elapsedTimeMs();
		
		game = new Game(context, gl);
		
		if(gameInitializedListener != null)
			gameInitializedListener.onGameInitialized();
		
		for (Entity ent : game.entList)
		{
			if (ent instanceof PuzzleBox)
			{
				((PuzzleBox)ent).setPuzzleInitListener(puzzleActivatedListener);
			}
		}
	}
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		//TODO keep so we can see what's slowing down a frame later.
		/*frameCount++;
		if (frameCount == 100)
			Debug.startMethodTracing("LDS_Game4");*/
		
		/*********************************
		 * Update World and Render Tiles *
		 *********************************/
		
		//clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		frameInterval = Stopwatch.elapsedTimeMs();
		
		//tick the stopwatch every frame, gives relatively stable intervals
		Stopwatch.tick();
				
		//iterate through triggers
		for (Trigger t : game.triggerList)
		{
			t.update();
		}
		
		//remove entities that are queued for removal
		game.cleaner.update(game.entList);
		
		//Triggered when the perspective needs to be redrawn
		if (windowOutdated)
		{
			updateCamPosition(gl);
			windowOutdated = false;
		}
				
		//Update which entities are rendered
		game.updateLocalEntities();
				
		//Render tileset
		for (Tile[] ts : game.tileset)
		{
			for (Tile t : ts)
			{
				if (t.isRendered())
				{
					gl.glTranslatef(t.getXPos(), t.getYPos(), 0.0f);
					t.draw(gl);
					gl.glLoadIdentity();
				}
			}
		}
		
		/******************
		 * Update Entites *
		 ******************/
		
		//move player and heldObject
		game.player.setAngle(game.joypad.getInputAngle());
		Vector2f playerMoveVec = Vector2f.scale(game.joypad.getInputVec(), (Stopwatch.elapsedTimeMs() - playerMoveTimeMs) * (game.player.getMoveSpeed() / 10000));
		game.player.setMoveInterpVec(playerMoveVec);
		game.player.setPos(Vector2f.add(game.player.getPos(), playerMoveVec));
		game.joypad.clearInputVec();
		if (game.player.isHoldingObject())
			game.player.updateHeldObjectPosition();

		//update all entites
		for (Entity ent : game.entList)
		{
			ent.update();
			//run AI code for enemies
			if (ent instanceof Enemy)
				game.runAI((Enemy)ent);
		}
		
		/**********************************************
		 * Perform a Collision Check for all Entities *
		 **********************************************/
		
		//Iterates through all entities
		for (int i = 0; i < game.entList.size(); i++)
		{
			Entity ent = game.entList.get(i);
						
			//checks for collision with all other entities in entList if needed
			if (Game.worldOutdated)
			{
				//checks collision and interacts with all other Entities
				for (int j = i + 1; j < game.entList.size(); j++)
				{
					Entity colEnt = game.entList.get(j);
					if (ent.isColliding(colEnt))
					{
						if(!ent.colList.contains(colEnt) && !colEnt.colList.contains(ent))
						{
							ent.colList.add(colEnt);
							colEnt.colList.add(ent);
							ent.interact(colEnt);
							colEnt.interact(ent);
						}
					}
					else if (ent.colList.contains(colEnt) || colEnt.colList.contains(ent))
					{
						//System.out.println(ent.colList.size() + " " + colEnt.colList.size());
						ent.colList.remove(colEnt);
						colEnt.colList.remove(ent);
						if (ent.colList.isEmpty())
						{
							ent.uninteract(colEnt);
							colEnt.uninteract(ent);
						}
					}
				}
				
				if (ent instanceof PhysEnt)
				{
					PhysEnt physEnt = (PhysEnt)ent;
					for (Tile[] ts : game.tileset)
					{
						for (Tile tile : ts)
						{
							if (tile.isRendered())
							{
								if (physEnt.isColliding(tile))
								{
									if (!physEnt.colList.contains(tile) && !tile.colList.contains(physEnt))
									{
										physEnt.colList.add(tile);
										tile.colList.add(physEnt);
										physEnt.tileInteract(tile);
									}
								}
								else if (physEnt.colList.contains(tile) || tile.colList.contains(physEnt))
								{
									//System.out.println(ent.colList.size() + " " + colEnt.colList.size());
									physEnt.colList.remove(tile);
									tile.colList.remove(physEnt);
									if (ent.colList.isEmpty())
										physEnt.tileUninteract(tile);
								}
							}
						}
					}
					
					//interacts with nearest tile to the entity; the tile it is standing on
					physEnt.onTileInteract(game.nearestTile(physEnt));
					
					//bounces PhysEnts appropriately, excluding objects held by the player
					if (!game.player.isHoldingObject() || physEnt != game.player.getHeldObject())
						physEnt.setPos(Vector2f.add(physEnt.getPos(), physEnt.getBounceVec()));
				}
				
				//moves the player correctly based on heldObject's bounceVecs
				if (ent == game.player && game.player.isHoldingObject())
				{
					game.player.setPos(Vector2f.add(game.player.getPos(), game.player.getHeldObject().getBounceVec()));
					game.player.updateHeldObjectPosition();
				}
				//runs new AI code for enemies
				else if (ent instanceof Enemy)
				{
					//TODO: recalculate new path for enemy to go on
				}
			}
			
			/***************************
			 * Performs Button Actions *
			 ***************************/
	
			//inside of ent for loop
			//checks for whatever happens when B is pressed.
			if (game.btnB.isPressed() && ent instanceof HoldObject)
			{
				if (!game.player.isHoldingObject()) //not holding anything and is close enough
				{
					if (game.player.closeEnough(ent) && game.player.isFacing(ent))
					{
						game.player.holdObject((HoldObject)ent);
						game.btnB.unpress();
					}
				}
				else //holding object, button pressed
				{
					game.player.dropObject();
					game.btnB.unpress();
				}
			}
		}
			
		//outside of ent for loop
		//causes button A to shoot when pressed
		if (game.btnA.isPressed())
		{
			if (!game.player.isHoldingObject() && game.player.getEnergy() != 0)
			{
				Vector2f directionVec = new Vector2f(game.player.getAngle());
				directionVec.scale(game.player.getHalfSize() + 20.0f);
				AttackBolt attack = new AttackBolt(Vector2f.add(game.player.getPos(), directionVec), directionVec, game.player.getAngle());
				vibrator(100);
				EntityManager.addEntity(attack);
				game.player.loseEnergy(10);
				SoundPlayer.getInstance().playSound(2);
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
				gl.glTranslatef(ent.getXPos(), ent.getYPos(), 0.0f);
				gl.glRotatef(ent.getAngle(), 0.0f, 0.0f, 1.0f);
				gl.glScalef(ent.getXScl(), ent.getYScl(), 1.0f);
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
			
			gl.glTranslatef(ent.getXPos(), ent.getYPos(), 0.0f);
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
		//System.out.println("FPS: " + (1000 / (Stopwatch.elapsedTimeMs() - frameInterval)));
		
		//TODO keep for later, if we want to see what's slowing down a frame.
		/*if (frameCount == 101)
		{
			testDebug = false;
			Debug.stopMethodTracing();
		}*/
	}

	public void vibrator(int time)
	{
		Vibrator vibrator = null; 
		try 
		{ 
			vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE); 
		} 
		catch (Exception e) {}
		
		if (vibrator != null)
		{ 
		  try 
		  { 
			  vibrator.vibrate(((long)time)); 
		  } 
		  catch (Exception e) {} 
		} 
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{		
		updateCamPosition(gl);
	}

	@Override
	public void onTouchInput(MotionEvent e) 
	{
		playerMoveTimeMs = Stopwatch.elapsedTimeMs();
		Stopwatch.tick();
		for(int i = 0; i < e.getPointerCount(); i++)
		{	
			Vector2f touchVec = new Vector2f(e.getX(i) - Game.screenW / 2, Game.screenH / 2 - e.getY(i));
			for (UIEntity ent : game.UIList)
			{
				if (touchVec.getX() >= ent.getXPos() - ent.getXSize() / 2 && touchVec.getX() <= ent.getXPos() + ent.getXSize() / 2 && touchVec.getY() >= ent.getYPos() - ent.getYSize() / 2 && touchVec.getY() <= ent.getYPos() + ent.getYSize() / 2)
				{				
					if (ent instanceof UIJoypad)
					{
						((UIJoypad)ent).setInputVec(e.getX(i) - Game.screenW / 2, Game.screenH / 2 - e.getY(i));
						windowOutdated = true;		
						Game.worldOutdated = true;	
					}
						
					//UIButton specific code
					if (ent instanceof UIButton)
					{
						UIButton btn = (UIButton)ent;
						
						//500ms delay between presses
						if (btn.canPress(500))
						{ 
							((UIButton)ent).press();
							btn.setIntervalTime(Stopwatch.elapsedTimeMs());
						}
					}
				}
			}
		}
	}
	//redraw the perspective
	public void updateCamPosition(GL10 gl)
	{
		gl.glViewport(0, 0, (int)Game.screenW, (int)Game.screenH);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, game.camPosX - (Game.screenW/2), game.camPosX + (Game.screenW/2), game.camPosY - (Game.screenH/2), game.camPosY + (Game.screenH/2));
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		game.updateLocalTileset();
	}
	
	//draw a screen-based perspective, push the world perspective onto the OpenGL matrix stack
	public void viewHUD(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, -Game.screenW /2 , Game.screenW / 2, -Game.screenH / 2, Game.screenH / 2);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
	}
	
	//pop that perspective back from the stack
	public void viewWorld(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();
	}
	
	@Override
	public void setGameOverEvent(OnGameOverListener listener) 
	{
		this.gameOverListener = listener;
		game.setGameOverEvent(listener);
	}
	
	@Override
	public void setGameInitializedEvent(OnGameInitializedListener listener)
	{
		this.gameInitializedListener = listener;
	}

	@Override
	public void setPuzzleActivatedEvent(OnPuzzleActivatedListener listener)
	{
		this.puzzleActivatedListener = listener;
	}
	
	@Override
	public void onPuzzleWon() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onPuzzleFailed() 
	{
		// TODO Auto-generated method stub
	}
	
	public void gameOver ()
	{
		gameOverListener.onGameOver();
	}
}
