package com.lds.puzzles.circuit;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.lds.puzzle.IPuzzle;
import com.lds.puzzle.event.*;

import com.lds.puzzles.circuit.KeyboardReader;
import com.lds.puzzles.circuit.Tile;
import com.lds.puzzles.circuit.dir;

public class CircuitPuzzle implements IPuzzle
{
	KeyboardReader reader = new KeyboardReader();
	private dir dir1, dir2;
	private int rand1, rand2;
	private boolean puzzleIsComplete;
	private Tile[][] grid = new Tile[3][6];
	private int xInput1, yInput1; //input for 1st selected tile
	private int xInput2, yInput2; //input for 2nd selected tile (to be switched)
	private int count0, count1, count2, count3, count4, count5, pairCount1, pairCount2; //keeps track of numbers of certain tile types (for sanity check)
	private boolean sane;
	
	public void init ()
	{
		//initialize global variables
		count0 = 0;
		count1 = 0;
		count2 = 0;
		count3 = 0;
		count4 = 0;
		count5 = 0;
		pairCount1 = 0;
		pairCount2 = 0;
		sane = false;
		puzzleIsComplete = false;
		
		//randomly initialize 6x3 grid of Tile objects
		initializeGrid();
		
		/**************
		 *sanity check*
		 **************/
		while (sane == false)
		{
			//count up different tile types
		    for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[0].length; j++)
				{
					if (grid[i][j].getType() == 0)
						count0++;
					if (grid[i][j].getType() == 1)
						count1++;
					if (grid[i][j].getType() == 2)
						count2++;
					if (grid[i][j].getType() == 3)
						count3++;
					if (grid[i][j].getType() == 4)
						count4++;
					if (grid[i][j].getType() == 5)
						count5++;
				}
			}
		    
		    //count up pairs
		    if (count1 >= count3)
				pairCount1 = count3;
			else
				pairCount1 = count1;
			if (count2 >= count4)
				pairCount2 = count4;
			else
				pairCount2 = count2;
				
		    //checks for three crucial pieces, if not found re-initializes grid
			if (pairCount1 < 1 || count1 < 1)
			{
				initializeGrid();
			}
			else
			{
				 
				if (count0 % 2 == 1) //if there is an odd number of vertical pieces
				{
					if (pairCount1 < count0 + (4 - ((count0 / 2) * 3)))
					{
						initializeGrid();
					}
					else if (pairCount2 < pairCount1 -1)
					{
						initializeGrid();
					}
					else
					{
						sane = true;
					}
				}
				else //if there is an even number of vertical pieces
				{
					if (pairCount1 < count0 + (2 - ((count0 / 2) * 3)))
					{
						initializeGrid();
					}
					else if (pairCount2 < pairCount1 - 2)
					{
						initializeGrid();
					}
					else
					{
						sane = true;
					}
				}
			}
			return;
		}
	}
	
	//randomly initialize a 6x3 grid of Tile objects
	public void initializeGrid ()
	{
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				rand1 = (int)(Math.random() * 4);
				rand2 = (int)(Math.random() * 4);
				
				while (rand1 == rand2)
				{
					rand2 = (int)(Math.random() * 4);
				}
				
				dir1 = dir.class.getEnumConstants()[rand1];
				dir2 = dir.class.getEnumConstants()[rand2];
				
				grid[i][j] = new Tile(dir1, dir2);
			}
		}
	}
	public void run ()
	{
		while (!puzzleIsComplete)
		{
			//for testing purposes, prints out grid (will be graphical later)
			printGrid();
			
			/* *****************************************
			 ***********GET USER INPUT******************
			 ****************************************** */
			
			//select a tile and highlights adjacent tiles, if a tile isn't already selected
			if (!grid[xInput2][yInput2].isSelected())
			{
				xInput1 = reader.readInt("X value: ");
				while (xInput1 < 0 || xInput1 > 2) //error check
				{
					System.out.println("Not valid.");
					xInput1 = reader.readInt("X value: ");
				}
				yInput1 = reader.readInt("Y value: ");
				while (yInput1 < 0 || yInput1 > 5) //error check
				{
					System.out.println("Not valid.");
					yInput1 = reader.readInt("Y value: ");
				}
				grid[xInput1][yInput1].select();
				highlightAdjacent(xInput1, yInput1);
				
				printGrid();
			}
			else //if the 2nd tile is already selected, make it the first tile
			{
				xInput1 = xInput2;
				yInput1 = yInput2;
			}
			
			//select 2nd tile
			xInput2 = reader.readInt("X value: ");
			while (xInput1 < 0 || xInput1 > 2) //error check
			{
				System.out.println("Not valid.");
				xInput1 = reader.readInt("X value: ");
			}
			yInput2 = reader.readInt("Y value: ");
			while (yInput1 < 0 || yInput1 > 5) //error check
			{
				System.out.println("Not valid.");
				yInput1 = reader.readInt("Y value: ");
			}
			
			//If double clicked, deselect tile and dehighlight adjacent tiles
			if (xInput1 == xInput2 && yInput1 == yInput2)
			{
				grid[xInput1][yInput1].deselect();
				dehighlightAdjacent(xInput1, yInput1);
			}
			
			//if tile is highlighted, switches tiles and dehighlights adjacent tiles
			else if (grid[xInput2][yInput2].isHightlighted())
			{
				dehighlightAdjacent(xInput1, yInput1);
				switchTiles(grid[xInput1][yInput1], grid[xInput2][yInput2]);
				grid[xInput1][yInput1].deselect();
				grid[xInput2][yInput2].deselect();
				
				printGrid();
			}
			
			//If it's not adjacent to the old tile, select the new tile
			else
			{
				dehighlightAdjacent(xInput1, yInput1);
				grid[xInput1][yInput1].deselect();
				grid[xInput2][yInput2].select();
				highlightAdjacent(xInput2, yInput2);
			}
			
			/* ***********************************************
			 * *****CHECKS FOR WIRE PATH AND POWERS WIRES*****
			 *************************************************/
			
			//runs recursive method
			rec (null, grid[0][0], 0, 0, dir.down);
			
			//unpowers entire grid, so broken circuts are turned off
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[0].length; j++)
				{
					grid[i][j].setPower(false);	
				}
			}
			
			//if last tile is powered and pointing down, complete puzzle
			if	(grid[grid.length - 1][grid[0].length - 1].isHightlighted() && (grid[grid.length - 1][grid[0].length - 1].getSide1() == dir.down || grid[grid.length - 1][grid[0].length - 1].getSide2() == dir.down))
			{
				puzzleIsComplete = true;
				System.out.println("Congratulations! You have won!!!!!1!111!1!!1!1111!!!!1111!!1one!!111!11111!11!!!!!!");
			}
		}
	}
	
	//main recursive method
	public void rec (Tile previous, Tile current, int x, int y, dir start)
	{
		dir newDir;
		int newX;
		int newY;
		if (current.getSide1() == Tile.returnFlippedTile(start))
			newDir = current.getSide2();
		else if (current.getSide2() == Tile.returnFlippedTile(start))
			newDir = current.getSide1();
		else
			return;
		newX = Tile.returnNewXPos(x, newDir);
		newY = Tile.returnNewYPos(y, newDir);
		current.setPower(true);
		if (!(x == 0 && newDir == dir.left))
		{
			if (!(x == grid.length - 1 && newDir == dir.right))
			{
				if (!(y == 0 && newDir == dir.up))
				{
					if (!(y == grid[0].length - 1 && newDir == dir.down))
					{
						rec(current, grid[newX][newY], newX, newY, newDir);
					}
				}
			}
		}
	}
	
	public boolean end ()
	{
		//KILL PROGRAM
		return false;
	}
	
	
	/***************
	 *OTHER METHODS*
	 ***************/
	
	//prints grid for testing purposes
	public void printGrid ()
	{
		for (int j = 0; j < grid[0].length; j++)
		{
			for (int i = 0; i < grid.length; i++)
			{
				System.out.print(grid[i][j]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	//highlight adjacent tiles to tile at given coordinates and checks that the adjacent tiles exist
	public void highlightAdjacent (int x, int y)
	{
		if (y != grid[0].length - 1)
			grid[x][y + 1].highlight();
		if (y != 0)
			grid[x][y - 1].highlight();
		if (x != grid.length - 1)
			grid[x + 1][y].highlight();
		if (x != 0)
			grid[x - 1][y].highlight();
	}
	
	//dehighlight adjacent tiles to tile at given coordinates and checks that the adjacent tiles exist
	public void dehighlightAdjacent (int x, int y)
	{
		if (y != grid.length - 1)
			grid[x][y + 1].dehighlight();
		if (y != 0)
			grid[x][y - 1].dehighlight();
		if (x != grid[0].length - 1)
			grid[x + 1][y].dehighlight();
		if (x != 0)
			grid[x - 1][y].dehighlight();
	}
	
	//switches two tiles using a temporary Tile object
	public void switchTiles (Tile tile1, Tile tile2)
	{
		Tile temp = new Tile(dir.up, dir.down);
		temp.setTile(tile2);
		tile2.setTile(tile1);
		tile1.setTile(temp);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawFrame(GL10 gl) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleInitializedListener(OnPuzzleInitializedListener listener) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleSuccessListener(OnPuzzleSuccessListener listener) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleFailListener(OnPuzzleFailListener listener) 
	{
		// TODO Auto-generated method stub
		
	}
}
