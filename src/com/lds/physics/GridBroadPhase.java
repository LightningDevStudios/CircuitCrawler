package com.lds.physics;

import java.util.ArrayList;
import com.lds.math.*;
import com.lds.game.entity.Entity;

public class GridBroadPhase 
{
	private World world;
	private int recursiveCount = 0, recursiveAccuracy, masterGridSide;
	
	private ArrayList<GridBox> masterGrid = new ArrayList<GridBox>();
	
	public GridBroadPhase(World world) throws Exception
	{
		if(world.Area < 9) { throw new Exception("World to small."); }
		
		//checks to see if world is 9^n
		while((Math.log(world.Area)/Math.log(9) % 1) != 0)
		{
			//if not make it 9^n
			world.Area++;
		}
		
		//Set recursion accuracy based on world size
		if(world.Area < 81)
			recursiveAccuracy = 0;
		if(world.Area > 81)
			recursiveAccuracy = 1;
		else if(world.Area > 729)
			recursiveAccuracy = 3;
		else if(world.Area > 6561)
			recursiveAccuracy = 5;
		else
			recursiveAccuracy = 7;
		
		masterGridSide = (int) Math.sqrt(world.Area);
		//masterGrid is world split into 9 sections
		masterGrid = CreateGrid(masterGridSide, new Vector2(0,0));
	}
	
	//Creates a grid of 9 boxes
	private ArrayList<GridBox> CreateGrid(float size, Vector2 center)
	{
		ArrayList<GridBox> grid = new ArrayList<GridBox>();
		int gridSize = (int) (size/3);
		
		//Calculate box centers
		Vector2[] boxPosList = 
		{
			new Vector2(-size/6,  		size/6),	
			new Vector2( center.getX(), size/6),
			new Vector2( size/6,  		size/6),
			new Vector2(-size/6,        center.getY()),
			new Vector2( center.getX(), center.getY()),
			new Vector2( size/6,        center.getY()),
			new Vector2(-size/6,        -size/6),
			new Vector2( center.getX(), -size/6),
			new Vector2( size/6,        -size/6),
		};
		
		//Calculate box sides
		for(int i = 0; i < boxPosList.length; i++)
		{
			float X1 = boxPosList[i].getX() - gridSize/2;
			float Y1 = boxPosList[i].getY() + gridSize/2;
			float X2 = boxPosList[i].getX() + gridSize/2;
			float Y2 = boxPosList[i].getY() - gridSize/2;
			
			grid.add(new GridBox(new Vector4(X1, X2, Y1, Y2), boxPosList[i], gridSize));
		}
		return grid;
	}
	
	private void StartBroadPhaseDetection(ArrayList<GridBox> grid)
	{
		//for each box in the grid if it has an entity mark it
		for(Entity ent : world.entList)
		{
			for(GridBox bx : grid)
			{
				if(bx.IsInBox(ent))
				{
					bx.entityCount++;
				}
			}
		}
		
		//For each box in the grid if its entity count is more than 1 and more recursion space is available split that box into a new grid
		//(repeat until entity count is less than 2 or recursions are used up)
		for(GridBox bx : grid)
		{
			if(bx.entityCount > 1)
			{
				if(recursiveCount < recursiveAccuracy)
				{
					recursiveCount++;
					StartBroadPhaseDetection(CreateGrid(bx.gridSize ,bx.center));
				}
				else
				{
					//Entities in bx are colliding
					//Do something here
				}
			}
		}
	}
	
	public void Run()
	{
		//Clear grid for new run
		if(masterGrid.size() > 0)
		{
			masterGrid.clear();
			masterGrid = CreateGrid(masterGridSide, new Vector2(0,0));
		}
		
		StartBroadPhaseDetection(masterGrid);
	}
}