package com.lds.game.entity;

public class Door extends PhysEnt
{
	private float closedX, closedY;
	
	public Door (float xPos, float yPos)
	{
		 super(72.0f, xPos, yPos, 0.0f, 1.0f, 0.5f, true, false, true, 100.0f, 100.0f, 0.0f);
		 closedX = xPos;
		 closedY = yPos;
	}

	public void open()
	{
		moveTo(closedX - size, closedY);
	}
	
	public void close()
	{	
		moveTo(closedX, closedY);
	}
	
	@Override
	public boolean doesCollide (Entity ent)
	{
		if (ent instanceof Tile)
			return false;
		else
			return super.doesCollide(ent);
	}
}
