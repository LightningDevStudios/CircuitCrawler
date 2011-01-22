package com.lds.trigger;

import com.lds.game.entity.PhysEnt;

public class CauseDoneScaling extends Cause
{
	private PhysEnt ent;
	private boolean readyToTrigger;
	
	public CauseDoneScaling(PhysEnt ent)
	{
		super();
		this.ent = ent;
		readyToTrigger = false;
	}
	
	@Override
	public void update()
	{
		if (!readyToTrigger)
		{
			if (ent.isScaling)
				readyToTrigger = true;
		}
		else
		{
			if (!ent.isScaling)
			{
				trigger();
				readyToTrigger = false;
			}
		}
	}
}
