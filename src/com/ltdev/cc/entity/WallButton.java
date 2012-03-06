package com.ltdev.cc.entity;

import com.ltdev.cc.SoundPlayer;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

public class WallButton extends Entity
{
	private boolean active;
	
	public WallButton(Vector2 position, float angle)
	{
	    super(new Rectangle(new Vector2(64, 64), position, angle, true));
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
