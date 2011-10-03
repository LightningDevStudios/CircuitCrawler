package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class World 
{
	public Vector2 size;
	public ArrayList<Entity> entList;
	public CollisionDetector collisionDetector;
	public PhysicsManager physManager;
	
	public World(Vector2 size, ArrayList<Entity> entList)
	{
		this.size = size;	
		this.entList = entList;
		collisionDetector = new CollisionDetector(size, entList);
		physManager = new PhysicsManager(entList);
	}
	
	public void PerformCollisionCheck()
	{
		collisionDetector.getMaster().setEntList(entList);
		collisionDetector.Run();
	}
}