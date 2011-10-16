package com.lds.game.entity;

import com.lds.game.SoundPlayer;
import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class Button extends Entity
{
	private boolean active;
	
	public Button(Vector2 position)
	{
		super(new Circle(69, position, false));
		active = false;
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
			SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
			updateTileset(1);
		}
	}
	
	public void deactivate()
	{
		if (active)
		{
			active = false;
			SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
			updateTileset(0);
		}
	}
	
	@Override
	public void interact(Entity ent)
	{	
		if (ent instanceof Player || ent instanceof HoldObject)
			activate();
	}
	
	@Override
	public void uninteract(Entity ent)
	{
		if (ent instanceof Player || ent instanceof HoldObject)
			deactivate();	
	}
}
