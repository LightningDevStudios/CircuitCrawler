package com.lds.game.entity;

public class Powerup extends PickupObj
{
	protected int value;
	
	public Powerup (int _value, float _xPos, float _yPos)
	{
		super(_xPos, _yPos);
		value = _value;
	}
	
	public int getValue ()
	{
		return value;
	}
	
}
