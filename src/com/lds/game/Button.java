package com.lds.game;

public class Button extends StaticObj
{
	protected boolean active;
	
	public Button (float _xPos, float _yPos)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
		active = false;
	}
	
	public Button (float _xPos, float _yPos, boolean _active)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
		active = _active;
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
		String entType = ent.getClass().getName().substring(13);
		
		if (entType.equals("Player"))
		{
			activate();
		}	
	}
	
	@Override
	public void uninteract (Entity ent) //runs when an entity stops colliding with Button
	{
		String entType = ent.getClass().getName().substring(13);
		
		if (entType.equals("Player"))
		{
			deactivate();
		}
	}
}
