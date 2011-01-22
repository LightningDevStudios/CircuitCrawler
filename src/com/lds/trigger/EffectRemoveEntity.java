package com.lds.trigger;

import com.lds.EntityCleaner;
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
		EntityCleaner.queueEntityForRemoval(ent);
	}
}
