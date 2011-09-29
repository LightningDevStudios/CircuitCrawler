package com.lds.physics;

import java.util.ArrayList;
import com.lds.math.*;
import com.lds.game.entity.Entity;

public class GridBroadPhase 
{
	private World world;
	private float masterGridSize;
	//private int recursions = 0, recursionsAccuracy = 2;
	
	private ArrayList<GridBox> masterGrid = new ArrayList<GridBox>();
	
	public GridBroadPhase(World world) throws Exception
	{
		int side = (int) Math.sqrt(world.Area);
		int gridSize = side/3;
		
		Vector2[] boxPosList = 
		{
			new Vector2(-side/6,  side/6),	
			new Vector2(   0, 	  side/6),
			new Vector2( side/6,  side/6),
			new Vector2(-side/6,    0   ),
			new Vector2(   0,       0   ),
			new Vector2( side/6,    0   ),
			new Vector2(-side/6, -side/6),
			new Vector2(   0,    -side/6),
			new Vector2( side/6, -side/6),
		};
		
		for(int i = 0; i < boxPosList.length; i++)
		{
			float X1 = boxPosList[i].getX() - gridSize/2;
			float Y1 = boxPosList[i].getY() + gridSize/2;
			float X2 = boxPosList[i].getX() + gridSize/2;
			float Y2 = boxPosList[i].getY() - gridSize/2;
			
			masterGrid.add(new GridBox(new Vector4(X1, X2, Y1, Y2)));
		}
	}
	
	public void Run()
	{
		for(Entity ent : world.entList)
		{
			for(GridBox bx : masterGrid)
			{
				if(bx.IsInBox(ent.getPos(), ent))
				{
					bx.entityCount++;
				}
			}
		}
		
		for(GridBox bx : masterGrid)
		{
			if(bx.entityCount > 1)
			{
				//Do Radius detect now
				//Then do SAT
			}
		}
	}
	
	/*
	public GridBroadPhase(World world) throws Exception
	{
		int size = (int) Math.sqrt(world.Area);
		
		if(size < 3)
			throw new Exception("World to small and/or not square.");
		
		this.world = world;
		
		int pow = 0;
		while(size > 1)
		{
			size /= 9;
			pow++;
		}
		
		size = (int)Math.pow(9, pow);
		
		masterGridSize = size/3;
		
		for(int i = 1; i < 10; i++)
		{
			//grids.add(new GridBox())
		}
	}
	
	public void Run()
	{		
		for(Entity ent : world.entList)
		{
			for(GridBox bx : masterGrid)
			{
				if(bx.IsInBox(ent.getPos()))
				{
					bx.entityCount++;
				}
			}
		}
		
		for(GridBox bx : masterGrid)
		{
			if(bx.entityCount > 1)
			{
				recursions = 0;
				
				if(CreateRecursionsGrid(bx))
				{
					//COLLISION HERE
				}
			}
		}
	}
	
	//True if Broad-phase detects collision
	public boolean CreateRecursionsGrid(GridBox bx)
	{
		recursions++;
		ArrayList<GridBox> smallGrid = new ArrayList<GridBox>();
		
		for(int i = 1; i < 10; i++)
		{
			//grids.add(new GridBox())
		}
		
		for(Entity ent : world.entList)
		{
			for(GridBox box : smallGrid)
			{
				if(box.IsInBox(ent.getPos()))
				{
					box.entityCount++;
				}
			}
		}
		
		for(GridBox box : smallGrid)
		{
			if(box.entityCount > 1)
			{
				if(recursions < recursionsAccuracy)
				{
					if(CreateRecursionsGrid(box)) return true;
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}*/
}