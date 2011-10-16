package com.lds.trigger;

import com.lds.EntityManager;
import com.lds.game.entity.Entity;

public class EffectRemoveEntity extends Effect
{
	private Entity ent;
	
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
