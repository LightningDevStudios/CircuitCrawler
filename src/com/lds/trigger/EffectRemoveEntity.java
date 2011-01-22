package com.lds.trigger;

import com.lds.EntityCleaner;
import com.lds.game.entity.Entity;

public class EffectRemoveEntity extends Effect
{
	private Entity ent;
	
	@Override
	public void fireOutput()
	{
		EntityCleaner.queueEntityForRemoval(ent);
	}
}
