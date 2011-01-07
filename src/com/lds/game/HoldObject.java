package com.lds.game;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl);
		held = false;
	}
	
	public boolean isHeld ()
	{
		return held;
	}
	
	public void hold ()
	{
		held = true;
	}
	
	public void drop ()
	{
		held = false;
		System.out.println("Shit just droppped");
	}
}
