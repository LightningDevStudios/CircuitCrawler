package com.lds.physics;

import java.util.ArrayList;
import com.lds.math.*;
import com.lds.game.entity.Entity;

public class GridBroadPhase 
{
	private World world;	
	private ArrayList<GridBox> masterGrid = new ArrayList<GridBox>();
	
	private final int MinGridSize = 5;
	
	public GridBroadPhase(World world) 
	{
		masterGrid = CreateGrid(world.size, new Vector2(0,0));
	}
	
	//Creates a grid of 4 boxes
	private ArrayList<GridBox> CreateGrid(Vector2 size, Vector2 center)
	{
		ArrayList<GridBox> grid = new ArrayList<GridBox>();
		int gridSizeX = (int) (size.getX()/2);
		int gridSizeY = (int) (size.getY()/2);
		
		//Calculate box centers
		Vector2[] boxPosList = 
		{
			new Vector2(-size.getX()/4, size.getY()/4),	
			new Vector2( center.getX(), size.getY()/4),
			new Vector2( size.getX()/4, size.getY()/4),
			new Vector2(-size.getX()/4, center.getY()),
		};
		
		//Calculate box sides
		for(int i = 0; i < boxPosList.length; i++)
		{
			float X1 = boxPosList[i].getX() - gridSizeX/2;
			float Y1 = boxPosList[i].getY() + gridSizeY/2;
			float X2 = boxPosList[i].getX() + gridSizeX/2;
			float Y2 = boxPosList[i].getY() - gridSizeY/2;
			
			grid.add(new GridBox(new Vector4(X1, X2, Y1, Y2), boxPosList[i], new Vector2(gridSizeX, gridSizeY)));
		}
		return grid;
	}
	
	private void StartBroadPhaseDetection(ArrayList<GridBox> grid)
	{
		//for each box in the grid if it has an entity mark it
		for(Entity ent : world.entList)
		{
			boolean onLine = true;
			for(GridBox bx : grid)
			{
				if(bx.IsInBox(ent))
				{
					bx.entityCount++;
					onLine = false;
				}
			}
			if(onLine)
			{
				//TODO do SAT with the entities that fall on lines in same grid
			}
		}
		
		for(GridBox bx : grid)
		{
			if(bx.entityCount > 1)
			{
				if(bx.gridSize.getX() > MinGridSize || bx.gridSize.getY() > MinGridSize)
					StartBroadPhaseDetection(CreateGrid(bx.gridSize ,bx.center));
				else
				{
					//TODO Do collision checks on bx entities.
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
			masterGrid = CreateGrid(world.size, new Vector2(0,0));
		}
		
		StartBroadPhaseDetection(masterGrid);
	}
}