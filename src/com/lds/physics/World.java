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
		physManager = new PhysicsManager(collisionDetector);
	}

	public void AddEntity (Entity ent) { entList.add(ent); }
	public void AddEntities (ArrayList<Entity> ents) { entList.addAll(ents); }
	public void AddEntities (Entity[] ents) { for(Entity ent : ents) entList.add(ent); }
	
	public void RemoveEntity(Entity ent) { entList.remove(ent); }
	public void RemoveEntities(ArrayList<Entity> ents) { entList.removeAll(ents); }
	public void RemoveEntities(Entity[] ents) { for(Entity ent : ents) entList.remove(ent); }
	
	public void ClearEntities() { entList.clear(); }
}