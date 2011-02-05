package com.lds;

import java.util.ArrayList;

import com.lds.game.entity.Entity;

public class EntityManager 
{
	private static ArrayList<Entity> trashList;
	private static ArrayList<Entity> addList;
	
	public EntityManager()
	{
		trashList = new ArrayList<Entity>();
		addList = new ArrayList<Entity>();
	}
	
	public static void removeEntity(Entity ent)
	{
		trashList.add(ent);
	}
	
	public static void addEntity(Entity ent)
	{
		addList.add(ent);
	}
	
	public void update(ArrayList<Entity> entList)
	{
		for (Entity ent : trashList)
		{
			entList.remove(ent);
			ent = null;
			trashList.remove(ent);
		}
		
		for (Entity ent : addList)
		{
			entList.add(ent);
			addList.remove(ent);
		}
	}
}
