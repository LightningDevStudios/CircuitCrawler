package com.lds.trigger;

import com.lds.game.entity.PhysEnt;

public class CauseDoneRotating extends Cause
{
	private PhysEnt ent;
	private boolean readyToTrigger;
	
	public CauseDoneRotating(PhysEnt ent)
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
			if (ent.isRotating)
				readyToTrigger = true;
		}
		else
		{
			if (!ent.isRotating)
			{
				trigger();
				readyToTrigger = false;
			}
		}
	}
}
