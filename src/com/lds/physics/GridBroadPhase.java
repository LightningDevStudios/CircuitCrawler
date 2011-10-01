package com.lds.physics;

import java.util.ArrayList;
import com.lds.math.*;
import com.lds.game.entity.Entity;

public class GridBroadPhase 
{
	private World world;	
	private Grid masterGrid;
	
	private final int MinGridSize = 5;
	
	public GridBroadPhase(World world) 
	{
		masterGrid = new Grid(world.size, new Vector2(0,0), 0);
	}
	
	private void StartBroadPhaseDetection(Grid grid)
	{
		//for each box in the grid if it has an entity mark it
		for(Entity ent : world.entList)
		{
			for(GridBox bx : grid.getGrids())
			{
				if(bx.IsInBox(ent))
				{
					bx.entityCount++;
				}
			}
		}
		
		for(GridBox bx : grid.getGrids())
		{
			if(bx.entityCount > 1)
			{
				grid.SplitGrid(bx.quad);
			}
		}
	}
	
	public void Run()
	{
		StartBroadPhaseDetection(masterGrid);
	}
}