package com.lds.game;

import com.lds.Enums.RenderMode;

public class Door extends PhysEnt
{
	private float closedX, closedY;
	
	public Door (float xPos, float yPos, RenderMode renderMode)
	{
		 super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 0.5f, true, renderMode, 69.0f, 100.0f, 1.0f);
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
	public void interact (Entity ent)
	{
		
	}
}
