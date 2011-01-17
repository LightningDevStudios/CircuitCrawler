package com.lds.game;

import com.lds.Enums.RenderMode;

public class Door extends PhysEnt
{
	private float closedX, closedY;
	
	public Door (float xPos, float yPos, RenderMode renderMode)
	{
		 super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 1.0f, true, renderMode, 100.0f, 100.0f, 1.0f);
		 closedX = xPos;
		 closedY = yPos;
	}

	public void open()
	{
		setPos(closedX + 200, closedY);
	}
	
	
	public void close()
	{	
		setPos(closedX, closedY);
	}
}
