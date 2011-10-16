package com.lds;

import com.lds.game.entity.Entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

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
		if (!trashList.contains(ent))
			trashList.add(ent);
	}
	
	public static void addEntity(Entity ent)
	{
		if (!addList.contains(ent))
			addList.add(ent);
	}
	
	public void update(ArrayList<Entity> entList, GL10 gl)
	{
		for (Entity ent : trashList)
		{
			ent.freeHardwareBuffers(gl);
			entList.remove(ent);
			ent = null;
		}
		
		for (Entity ent : addList)
		{
			ent.genHardwareBuffers(gl);
			entList.add(ent);
		}
		
		trashList.clear();
		addList.clear();
	}
}
