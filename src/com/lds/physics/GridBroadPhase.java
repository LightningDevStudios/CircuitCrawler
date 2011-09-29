package com.lds.physics;

import java.util.ArrayList;
import com.lds.math.*;
import com.lds.game.entity.Entity;

public class GridBroadPhase 
{
	private World world;
	private float masterGridSize;
	private int recursiveCount = 0, recursiveAccuracy = 5;
	
	private ArrayList<GridBox> masterGrid = new ArrayList<GridBox>();
	
	public GridBroadPhase(World world) throws Exception
	{
		masterGrid = CreateGrid((int) Math.sqrt(world.Area), new Vector2(0,0));
	}
	
	private ArrayList<GridBox> CreateGrid(float size, Vector2 center)
	{
		ArrayList<GridBox> grid = new ArrayList<GridBox>();
		int gridSize = (int) (size/3);
		
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
		for(Entity ent : world.entList)
		{
			for(GridBox bx : grid)
			{
				if(bx.IsInBox(ent.getPos(), ent))
				{
					bx.entityCount++;
				}
			}
		}
		
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
		StartBroadPhaseDetection(masterGrid);
	}
}