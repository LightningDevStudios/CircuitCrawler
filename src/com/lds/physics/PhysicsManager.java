package com.lds.physics;

public class PhysicsManager 
{
	private World world;
	private GridBroadPhase grid;
	
	public PhysicsManager(World world, GridBroadPhase grid)
	{
		this.world = world;
		this.grid = grid;
	}
	
	public void PerformCollisionCheck()
	{
		
	}
}
