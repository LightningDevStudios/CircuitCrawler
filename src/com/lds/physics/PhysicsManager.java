package com.lds.physics;

public class PhysicsManager 
{
	private World world;
	private CollisionDetector grid;
	
	public PhysicsManager(World world, CollisionDetector grid)
	{
		this.world = world;
		this.grid = grid;
	}
	
	public void PerformCollisionCheck()
	{
		grid.Run();
	}
}
