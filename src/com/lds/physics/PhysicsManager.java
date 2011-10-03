package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

public class PhysicsManager 
{
	/***********
	 * Members *
	 ***********/

	//TODO: Remove entlist
	private ArrayList<Entity> entList;
	
	/****************
	 * Constructors *
	 ****************/
	
	public PhysicsManager()
	{
		
	}
	public PhysicsManager(ArrayList<Entity> entList)
	{
		this.entList = entList;
	}
	
	public void AddEntity (Entity ent) { entList.add(ent); }
	public void AddEntities (ArrayList<Entity> ents) { entList.addAll(ents); }
	public void AddEntities (Entity[] ents) 
	{ 
		for(Entity ent : ents) 
			entList.add(ent);
	}
	
	public void RemoveEntity(Entity ent) { entList.remove(ent); }
	public void RemoveEntities(ArrayList<Entity> ents) { entList.removeAll(ents); }
	public void RemoveEntities(Entity[] ents)
	{
		for(Entity ent : ents) 
			entList.remove(ent);
	}
	
	public void ClearEntities() { entList.clear(); }
}
