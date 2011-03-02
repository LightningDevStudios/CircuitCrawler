package com.lds.game.entity;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, true, 100.0f, 90.0f, 1.0f);
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
		moveInterpVec.set(0, 0);
	}
}
