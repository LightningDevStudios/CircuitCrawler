package com.lds.game.entity;

import com.lds.game.SoundPlayer;
import com.lds.math.Vector2;
import com.lds.physics.primatives.Rectangle;

public class WallButton extends Entity
{
	private boolean active;
	
	public WallButton(Vector2 position, float angle)
	{
	    super(new Rectangle(64, position, angle, true));
		active = false;
		
		this.tilesetX = 0;
		this.tilesetY = 1;
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (active)
		{
			active = false;
            SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
		}
		else
		{
			active = true;
			SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
		}
	}
	
	public boolean isActive()
	{
		return active;
	}
}
