package com.lds.game.entity;

import com.lds.game.SoundPlayer;

public class Button extends StaticEnt
{
	private boolean active;
	
	public Button(float xPos, float yPos)
	{
		super(69.0f, xPos, yPos, 0.0f, 1.0f, 1.0f, true, true, false);
		active = false;
		circular = true;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void activate()
	{
		if (!active)
		{
			active = true;
			SoundPlayer.getInstance().playSound(SoundPlayer.SOUND_TEST);
		}
	}
	
	public void deactivate()
	{
		if (active)
		{
			active = false;
			SoundPlayer.getInstance().playSound(SoundPlayer.SOUND_TEST);
		}
	}
	
	@Override
	public void interact(Entity ent)
	{	
		if (ent instanceof Player || ent instanceof HoldObject)
		{
			activate();
			updateTileset(1);
		}	
	}
	
	@Override
	public void uninteract(Entity ent) //runs when an entity stops colliding with Button
	{
		if (ent instanceof Player || ent instanceof HoldObject)
		{
			deactivate();
			updateTileset(0);
		}	
	}
}
