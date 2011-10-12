package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class World 
{
	private Vector2 size;
	private ArrayList<Entity> entList;
	private CollisionDetector collisionDetector;
	private PhysicsManager physManager;
	
	public World(Vector2 size, ArrayList<Entity> entList)
	{
		this.size = size;	
		this.entList = entList;
		collisionDetector = new CollisionDetector(size, entList);
		physManager = new PhysicsManager(collisionDetector);
	}

	public void addEntity(Entity ent)
	{ 
	    entList.add(ent);
	}
	
	public void addEntities(ArrayList<Entity> ents)
	{
	    entList.addAll(ents);
	}
	
	public void addEntities(Entity[] ents)
	{
	    for (Entity ent : ents)
	        entList.add(ent);
	}
	
	public void removeEntity(Entity ent)
	{
	    entList.remove(ent);
	}
	
	public void removeEntities(ArrayList<Entity> ents)
	{
	    entList.removeAll(ents);
	}
	
	public void removeEntities(Entity[] ents)
	{
	    for (Entity ent : ents)
	        entList.remove(ent);
	}
	
	public void clearEntities()
	{
	    entList.clear();
	}
}