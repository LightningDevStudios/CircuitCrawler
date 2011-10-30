package com.lds;

import com.lds.game.entity.Entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

/**
 * A class that handles the addition and removal of Entities from anywhere.
 * Created to prevent errors relating to modifying an ArrayList while iterating through it.
 * @author Lightning Development Studios
 */
public class EntityManager 
{
	private static ArrayList<Entity> trashList;
	private static ArrayList<Entity> addList;
	
	/**
	 * Initializes a new instance of the EntityManager class.
	 */
	public EntityManager()
	{
		trashList = new ArrayList<Entity>();
		addList = new ArrayList<Entity>();
	}
	
	/**
	 * Queues an Entity for removal.
	 * @param ent The Entity to removed.
	 */
	public static void removeEntity(Entity ent)
	{
		if (!trashList.contains(ent))
			trashList.add(ent);
	}
	
	/**
	 * Queues an Entity for addition.
	 * @param ent The Entity to add.
	 */
	public static void addEntity(Entity ent)
	{
		if (!addList.contains(ent))
			addList.add(ent);
	}
	
	/**
	 * Adds and removes all the queued Entities since the last call to update.
	 * @param entList The {@code ArrayList<Entity>} to operate on.
	 * @param gl An OpenGL context used to delete the VBOs.
	 * \todo split the hardware buffer removal into another method, call that one first? Or will we later assume a GL11 instead of a GL10?
	 */
	public void update(ArrayList<Entity> entList, GL11 gl)
	{
		for (Entity ent : trashList)
		{
			ent.freeHardwareBuffers(gl);
			entList.remove(ent);
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
