package com.lds.game;

import com.lds.Enums.RenderMode;

public class Door extends PhysEnt
{
	private float permanentX, permanentY;
	
	public Door (float xPos, float yPos, RenderMode renderMode)
	{
		 super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 0.5f, true, renderMode, 10.0f, 69.0f, 0.069f);
		 permanentX = xPos;
		 permanentY = yPos;
	}

	public void open ()
	{
		moveTo(1000.0f, 1000.0f);
		//moveTo(permanentX + Entity.DEFAULT_SIZE, permanentY);
	}
	
	
	public void close ()
	{	
		moveTo(-(permanentX + Entity.DEFAULT_SIZE), permanentY);
	}
}
