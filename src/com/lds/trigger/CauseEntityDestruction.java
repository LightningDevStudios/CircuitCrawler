package com.lds.trigger;

import com.lds.game.entity.Entity;

public class CauseEntityDestruction extends Cause
{
	private Entity ent;
	
	public CauseEntityDestruction(Entity ent)
	{
		super();
		this.ent = ent;
	}
	
	@Override
	public void update()
	{
		if (!ent.exists())
			trigger();
		
		else
			untrigger();
	}
}
