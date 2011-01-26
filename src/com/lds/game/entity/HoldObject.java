package com.lds.game.entity;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, 100.0f, 90.0f, 5.0f);
		held = false;
	}
	
	@Override
	public void update()
	{
		super.update();
	}
	
	@Override
	public void collide(Entity ent)
	{
		
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
