package com.ltdev.cc.trigger;

import com.ltdev.EntityManager;
import com.ltdev.cc.entity.Entity;
import com.ltdev.trigger.Effect;

/**
 * An Effect that removes an entity from the game when fired.
 * @author Lightning Development Studios
 */
public class EffectRemoveEntity extends Effect
{
	private Entity ent;
	
	/**
	 * Initializes a new instance of the EffectRemoveEntity class.
	 * @param ent The entity to remove.
	 */
	public EffectRemoveEntity(Entity ent)
	{
		super();
		this.ent = ent;
	}
	
	@Override
	public void fireOutput()
	{
	    if (ent != null)
	    {
	        EntityManager.removeEntity(ent);
	        ent = null;
	    }
	}
	
	@Override
	public void unfireOutput()
	{
	    
	}
}
