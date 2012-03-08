package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.Shape;

public abstract class HoldObject extends Entity
{
	private boolean held;
	
	public HoldObject(Shape shape)
	{
	    super(shape);
		held = false;
	}
	
	public boolean isHeld()
	{
		return held;
	}
	
	public void hold()
	{
		held = true;
	}
	
	public void drop()
	{
		held = false;
	}
}
