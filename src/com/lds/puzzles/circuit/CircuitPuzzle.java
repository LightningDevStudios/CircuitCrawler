package com.lds.puzzles.circuit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.view.MotionEvent;

import com.lds.game.Game;
import com.lds.game.R;
import com.lds.game.puzzle.IPuzzle;
import com.lds.game.puzzle.event.*;

import com.lds.puzzles.circuit.Tile;

import com.lds.Enums.Direction;
import com.lds.Stopwatch;
import com.lds.Texture;
import com.lds.TextureLoader;

public class CircuitPuzzle implements IPuzzle
{
	private Tile[][] grid;
	private Texture tileTex;
	private Context context;
	private Object syncObj;
		
	private OnPuzzleInitializedListener initializedListener;
	private OnPuzzleSuccessListener successListener;
	private OnPuzzleFailListener failListener;
	
	private boolean selected;
	private int touchEventTime;
	private int selectedX, selectedY;
						
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		tileTex = new Texture(R.drawable.circuitpuzzle, 128, 128, 8, 8, context, "circuitpuzzle");
		TextureLoader tl = TextureLoader.getInstance();
		tl.initialize(gl);
		tl.loadTexture(tileTex);
		
		initializePuzzle();
		
		Stopwatch.start();
		Stopwatch.tick();
		
		if (initializedListener != null)
			initializedListener.onInitialized();
		
	}

	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Stopwatch.tick();
		
		for (Tile[] ta : grid)
		{
			for (Tile t : ta)
			{
				gl.glTranslatef(t.getXPos(), t.getYPos(), 0.0f);
				t.draw(gl);
				gl.glLoadIdentity();
			}
		}
		
		synchronized (syncObj)
		{
			syncObj.notify();
		}
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		gl.glViewport(0, 0, (int)Game.screenW, (int)Game.screenH);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0, width, height, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void onTouchEvent(MotionEvent event) 
	{
		touchEventTime += Stopwatch.getFrameTime();
		if (touchEventTime > 500)
		{
			touchEventTime = 0;
			float xInput = event.getX();
			float yInput = event.getY();
						
			//initial touch
			if (!selected)
			{
				for (int i = 0; i < grid.length; i++)
				{
					for (int j = 0; j < grid[0].length; j++)
					{
						Tile t = grid[i][j];
						if (xInput >= t.getXPos() - (Tile.tileSize / 2) &&
							xInput < t.getXPos() + (Tile.tileSize / 2) &&
							yInput >= t.getYPos() - (Tile.tileSize / 2) &&
							yInput < t.getYPos() + (Tile.tileSize / 2))
						{
							selected = true;
							selectedX = j;
							selectedY = i;
							t.select();
							highlightAdjacent(j, i);
						}
					}
				}
			}
			
			//second touch
			else
			{
				boolean correctTouch = false;
				for (int i = 0; i < grid.length; i++)
				{
					for (int j = 0; j < grid[0].length; j++)
					{
						Tile t = grid[i][j];
						if (xInput >= t.getXPos() - (Tile.tileSize / 2) &&
							xInput < t.getXPos() + (Tile.tileSize / 2) &&
							yInput >= t.getYPos() - (Tile.tileSize / 2) &&
							yInput < t.getYPos() + (Tile.tileSize / 2) &&
							t.isHightlighted())
						{
							swapTiles(t, grid[selectedY][selectedX]);
							correctTouch = true;
						}
					}
				}
				if (!correctTouch)
				{
					grid[selectedY][selectedX].deselect();
					for (Tile[] ta : grid)
					{
						for (Tile t : ta)
						{
							if (t.isHightlighted())
								t.dehighlight();
						}
					}
				}
				
				selected = false;
			}
		}
	}

	public void setPuzzleInitializedEvent(OnPuzzleInitializedListener listener) 
	{
		this.initializedListener = listener;
	}

	public void setPuzzleSuccessEvent(OnPuzzleSuccessListener listener) 
	{
		this.successListener = listener;
		
	}

	public void setPuzzleFailEvent(OnPuzzleFailListener listener) 
	{
		this.failListener = listener;
		
	}
	
	public void highlightAdjacent (int x, int y)
	{
		if (y < grid.length - 1)
			grid[y + 1][x].highlight();
		if (y > 0)
			grid[y - 1][x].highlight();
		if (x < grid[0].length - 1)
			grid[y][x + 1].highlight();
		if (x > 0)
			grid[y][x - 1].highlight();
	}
	
	public void swapTiles(Tile t0, Tile t1)
	{
		TileType type = t0.getType();
		t0.setTileType(t1.getType());
		t1.setTileType(type);
		
		//update the power between the wires
		for (Tile[] ta : grid)
		{
			for (Tile t : ta)
			{
				t.unpower();	
			}
		}
		checkPower(0, 0, Direction.DOWN);
		
		//if last tile is powered and pointing down, complete puzzle
		Tile lastTile = grid[grid.length - 1][grid[0].length - 1];
		if	(lastTile.isPowered() && lastTile.containsDirection(Direction.DOWN))
		{
			if (successListener != null)
				successListener.onPuzzleSuccess();
		}
	}
	
	public void checkPower(int x, int y, Direction incomingDir)
	{
		int newX;
		int newY;
		Direction outgoingDir;
		Tile current = grid[y][x];
		
		if (current.getType().getDir1() == Tile.flipDirection(incomingDir))
			outgoingDir = current.getType().getDir2();
		else if (current.getType().getDir2() == Tile.flipDirection(incomingDir))
			outgoingDir = current.getType().getDir1();
		else
			return;
		
		current.power();
		
		newX = Tile.moveXPos(x, outgoingDir);
		newY = Tile.moveYPos(y, outgoingDir);
		
		if (newX >= 0 && newX < grid[0].length && newY >= 0 && newY < grid.length)
			checkPower(newX, newY, outgoingDir);
	}
	
	public void initializePuzzle()
	{
		int countVert = 0;
		int countHoriz = 0;
		int countBL = 0;
		int countTL = 0;
		int countTR = 0;
		int countBR = 0;
		int pairCount1 = 0;
		int pairCount2 = 0;
		
		boolean solvable = false;

		
		
		initializeGrid();
		
		while (!solvable)
		{
			countVert = 0;
			countHoriz = 0;
			countBL = 0;
			countTL = 0;
			countTR = 0;
			countBR = 0;
			
			//count up different tile types
		    for (Tile[] ta : grid)
			{
				for (Tile t : ta)
				{
					switch (t.getType())
					{
						case VERTICAL: 		countVert++; break;
						case HORIZONTAL: 	countHoriz++; break;
						case BOTTOMLEFT:	countBL++; break;
						case TOPLEFT:		countTL++; break;
						case TOPRIGHT:		countTR++; break;
						case BOTTOMRIGHT:	countBR++; break;
						default:
					}
				}
			}
		    
		    //count up pairs
		    if (countBR >= countTL)
				pairCount1 = countTL;
			else
				pairCount1 = countBR;
		    
			if (countBL >= countTR)
				pairCount2 = countTR;
			else
				pairCount2 = countBL;
				
		    //checks for three crucial pieces, if not found re-initializes grid
			if (pairCount1 < 1 || countHoriz < 1)
				initializeGrid();
			
			else
			{
				 
				if (countVert % 2 == 1) //if there is an odd number of vertical pieces
				{
					if (pairCount1 < countVert + (4 - ((countVert / 2) * 3)) || pairCount2 < pairCount1 - 1)
						initializeGrid();
					
					else
						solvable = true;
				}
				else //if there is an even number of vertical pieces
				{
					if (pairCount1 < countVert + (2 - ((countVert / 2) * 3)) || pairCount2 < pairCount1 - 2)
						initializeGrid();
					
					else
						solvable = true;
				}
			}
		}
	}
	
	//randomly initialize a 6x3 grid of Tile objects
	public void initializeGrid ()
	{
		grid = new Tile[3][6];
		
		float vertSize = Game.screenH / (grid.length + 2);
		float horizSize = Game.screenW / (grid[0].length + 2);
		
		if (vertSize > horizSize)
			Tile.tileSize = horizSize;
		else
			Tile.tileSize = vertSize;
		
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				int rand1 = (int)(Math.random() * 6);
				int rand2 = (int)(Math.random() * 6);
				
				while (rand1 == rand2)
				{
					rand2 = (int)(Math.random() * 6);
				}
				
				TileType type = TileType.class.getEnumConstants()[rand1];
				grid[i][j] = new Tile((j + 1) * Tile.tileSize, (i + 1) * Tile.tileSize, tileTex, type);
			}
		}
	}
	
	public void setContext(Context context)
	{
		this.context = context;
		
	}
	
	public void setSyncObj(Object o)
	{
		syncObj = o;
	}
}
