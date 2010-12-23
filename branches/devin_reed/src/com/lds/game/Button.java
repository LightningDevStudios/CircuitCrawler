package com.lds.game;

public class Button extends StaticObj
{
	private boolean active;
	
	public Button ()
	{
		active = false;
	}
	
	public Button (boolean _active)
	{
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
	public void uninteract (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13);
		
		if (entType.equals("Player"))
		{
			deactivate();
		}
	}
}
