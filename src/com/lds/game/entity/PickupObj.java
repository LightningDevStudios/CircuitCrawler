package com.lds.game.entity;

import com.lds.EntityManager;

public abstract class PickupObj extends PhysEnt //pickup objects are picked up, such as keys, powerups, or batteries
{	
	public PickupObj (float xPos, float yPos)
	{
		super(10.0f, xPos, yPos, false, false, 50.0f, 90.0f, 0.1f);
	}
	
	@Override
	public void update()
	{
		if (getXScl() == 1.0f)
		{
			scaleTo(2.0f, 2.0f);
		}
		else if (getXScl() == 2.0)
		{
			scaleTo(1.0f, 1.0f);
		}
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (ent instanceof Player)
		{
			EntityManager.removeEntity(this);
		}
	}
}
