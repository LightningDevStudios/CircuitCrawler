package com.lds.game;

public class InvenPickup extends PickupObj
{
	protected String name;
	
	public InvenPickup (String _name, float _xPos, float _yPos)
	{
		super(_xPos, _yPos);
		name = _name;
	}
	
	public String getName ()
	{
		return name;
	}
}
