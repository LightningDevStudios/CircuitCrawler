package com.lds.game.entity;

public class Button extends StaticEnt
{
	private boolean active;
	
	public Button (float xPos, float yPos)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, 0.0f, 1.0f, 1.0f, true, true);
		active = false;
		circular = true;
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