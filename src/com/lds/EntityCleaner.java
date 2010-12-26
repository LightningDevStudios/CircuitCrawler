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
		for (int i = 0; i < trashList.size(); i++)
		{
			Entity ent = trashList.get(i);
			ent = null;
			entList.remove(trashList.get(i));
			trashList.remove(i);
		}
	}
}
