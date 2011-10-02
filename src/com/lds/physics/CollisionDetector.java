package com.lds.physics;

import com.lds.math.*;
import com.lds.game.entity.Entity;

public class CollisionDetector 
{
	private World world;	
	private Grid masterGrid;
	
	public CollisionDetector(World world) 
	{
		masterGrid = new Grid(world.size, new Vector2(0,0), 0, world.entList);
	}
	
	public void Run()
	{
		masterGrid = new Grid(world.size, new Vector2(0,0), 0, world.entList);	
		masterGrid.FindAndUpdateEntitiesInAQuadrant(masterGrid);
	}
}