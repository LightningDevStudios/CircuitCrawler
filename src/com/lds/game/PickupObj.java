package com.lds.game;

public abstract class PickupObj extends PhysEnt //pickup objects are picked up, such as keys, powerups, or batteries
{	
	public PickupObj (float _xPos, float _yPos)
	{
		super(10.0f, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
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
}
