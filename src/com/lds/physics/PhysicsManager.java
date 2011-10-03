package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

public class PhysicsManager 
{
	private CollisionDetector grid;
	private ArrayList<Entity> entList = new ArrayList<Entity>();
	
	public PhysicsManager(Vector2 worldSize) { grid = new CollisionDetector(worldSize, entList); }
	public PhysicsManager(Vector2 worldSize, ArrayList<Entity> entList) { grid = new CollisionDetector(worldSize, entList); }
	
	public void PerformCollisionCheck()
	{
		grid.getMaster().setEntList(entList);
		grid.Run();
	}
	
	public void AddEntity (Entity ent) { entList.add(ent); }
	public void AddEntity (ArrayList<Entity> ents) { entList.addAll(ents); }
	public void AddEntity (Entity[] ents) { for(Entity ent : ents) { entList.add(ent); } }
	public void RemoveEntity(Entity ent) { entList.remove(ent); }
	public void RemoveEntity(ArrayList<Entity> ents) { entList.removeAll(ents); }
	public void RemoveEntity(Entity[] ents) { for(Entity ent : ents) { entList.remove(ent); } }
	public void ClearEntity() { entList.clear(); }
}
