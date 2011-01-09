package com.lds.game;

import com.lds.Enums.RenderMode;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, renderMode, 0.0f, 0.0f, 0.0f);
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
	}
}
