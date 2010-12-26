package com.lds.game;

import com.lds.EntityCleaner;

public abstract class PickupObj extends Entity //pickup objects are picked up, such as keys, powerups, or batteries
{
	
	public PickupObj (float _xPos, float _yPos)
	{
		super(10.0f, _xPos, _yPos, 0.0f, 1.0f, 1.0f, false, true, Entity.DEFAULT_SPEED);
	}
	
	@Override
	public void pickupScale () //pickup objects will constantly scale up and down using this method
	{
		if (xScl == 1.0f)
		{
			scale(2.0f, 2.0f);
		}
		else if (xScl == 2.0)
		{
			scale(0.5f, 0.5f);
		}
	}
	
	@Override
	public void interact (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13); //gets the type of class
		
		if (entType.equals("Player"))
		{
			EntityCleaner.queueEntityForRemoval(this);
		}
	}
}
