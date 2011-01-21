package com.lds.game.entity;

import com.lds.Enums.RenderMode;

public class Button extends StaticEnt
{
	private boolean active;
	private Door d;
	
	public Button (float xPos, float yPos, RenderMode renderMode, Door d)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 1.0f, true, true, renderMode);
		active = false;
		this.d = d;
		circular = true;
	}
	
	public boolean isActive ()
	{
		return active;
	}
	
	public void activate ()
	{
		active = true;
		d.open();
	}
	
	public void deactivate ()
	{
		active = false;
		d.close();
	}
	
	@Override
	public void interact (Entity ent)
	{	
		if (ent instanceof Player || ent instanceof HoldObject)
		{
			activate();
			updateTileset(1);
		}	
	}
	
	@Override
	public void uninteract (Entity ent) //runs when an entity stops colliding with Button
	{
		if (ent instanceof Player || ent instanceof HoldObject)
		{
			deactivate();
			updateTileset(0);
		}	
	}
}
