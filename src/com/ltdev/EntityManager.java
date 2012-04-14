/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.physics.World;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

/**
 * A class that handles the addition and removal of Entities from anywhere.
 * Created to prevent errors relating to modifying an ArrayList while iterating through it.
 * @author Lightning Development Studios
 */
public final class EntityManager
{
	private static ArrayList<Entity> trashList = new ArrayList<Entity>();
	private static ArrayList<Entity> addList = new ArrayList<Entity>();
	
	/**
	 * Prevents initialization of EntityManager.
	 */
	private EntityManager()
	{
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
	 * @param physWorld The physics world.
	 * @param gl An OpenGL context used to delete the VBOs.
	 * \todo split the hardware buffer removal into another method, call that one first? Or will we later assume a GL11 instead of a GL10?
	 */
	public static void update(ArrayList<Entity> entList, World physWorld, GL11 gl)
	{
		for (Entity ent : trashList)
		{
			ent.unload(gl);
			entList.remove(ent);
			physWorld.remove(ent.getShape());
		}
		
		for (Entity ent : addList)
		{
			ent.initialize(gl);
			entList.add(ent);
			physWorld.add(ent.getShape());
		}
		
		trashList.clear();
		addList.clear();
	}
}
