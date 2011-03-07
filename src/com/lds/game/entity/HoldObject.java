package com.lds.game.entity;

import com.lds.Vector2f;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, float friction)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, true, 100.0f, 90.0f, 2.0f, friction);
		held = false;
	}
	
	@Override
	public void onTileInteract(Tile tile)
	{
		if (!held)
			super.onTileInteract(tile);
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
		push(new Vector2f (angle).scale(4));
	}
	
	public void push ()
	{
		held = false;
		stop();
	}
}
