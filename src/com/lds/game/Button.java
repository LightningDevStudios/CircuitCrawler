package com.lds.game;

import com.lds.Enums.RenderMode;

public class Button extends StaticEnt
{
	protected boolean active;
	
	public Button (float xPos, float yPos, RenderMode renderMode)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 1.0f, false, renderMode);
		active = false;
	}
	
	public Button (float xPos, float yPos, RenderMode renderMode, boolean active)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 1.0f, false, renderMode);
		this.active = active;
	}
	
	public boolean isActive ()
	{
		return active;
	}
	
	public void activate ()
	{
		active = true;
	}
	
	public void deactivate ()
	{
		active = false;
	}
	
	@Override
	public void interact (Entity ent)
	{	
		if (ent instanceof Player || ent instanceof PhysBlock)
		{
			activate();
		}	
	}
	
	@Override
	public void uninteract (Entity ent) //runs when an entity stops colliding with Button
	{
		if (ent instanceof Player || ent instanceof PhysBlock)
		{
			activate();
		}	
	}
}
