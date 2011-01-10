package com.lds;

import java.util.ArrayList;
import com.lds.game.Entity;

public class EntityCleaner 
{
	private static ArrayList<Entity> trashList;
	
	public EntityCleaner()
	{
		trashList = new ArrayList<Entity>();
	}
	
	public static void queueEntityForRemoval(Entity ent)
	{
		trashList.add(ent);
	}
	
	public void clean(ArrayList<Entity> entList)
	{
		for (Entity ent : trashList)
		{
			entList.remove(ent);
			ent = null;
			trashList.remove(ent);
		}
	}
}
