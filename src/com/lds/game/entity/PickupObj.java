package com.lds.game.entity;

import com.lds.Enums.RenderMode;

public abstract class PickupObj extends PhysEnt //pickup objects are picked up, such as keys, powerups, or batteries
{	
	public PickupObj (float _xPos, float _yPos)
	{
		super(10.0f, _xPos, _yPos, false, RenderMode.TILESET, 50.0f, 90.0f, 0.1f);
	}
	
	@Override
	public void update()
	{
		pickupScale();
	}
	
	public void pickupScale() //pickup objects will constantly scale up and down using this method
	{
		if (getXScl() == 1.0f)
		{
			scale(2.0f, 2.0f);
		}
		else if (getXScl() == 2.0)
		{
			scale(0.5f, 0.5f);
		}
	}
}
