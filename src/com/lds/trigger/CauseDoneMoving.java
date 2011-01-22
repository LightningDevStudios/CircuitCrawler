package com.lds.trigger;

import com.lds.game.entity.PhysEnt;

public class CauseDoneMoving extends Cause
{
	private PhysEnt ent;
	private boolean readyToTrigger;
	@Override
	public void update()
	{
		if (!readyToTrigger)
		{
			if (ent.isMoving)
				readyToTrigger = true;
		}
		else
		{
			if (!ent.isMoving)
			{
				trigger();
				readyToTrigger = false;
			}
		}
	}
}
