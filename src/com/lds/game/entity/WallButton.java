package com.lds.game.entity;

import com.lds.game.SoundPlayer;

public class WallButton extends StaticEnt
{
	private boolean active;
	
	public WallButton(float xPos, float yPos, float angle)
	{
		super(64.0f, xPos, yPos, angle, 0.2f, 0.0f, true, false, true);
		active = false;
	}
	
	@Override
	public void interact (Entity ent)
	{
		if (ent instanceof PhysEnt)
		{
			if (active)
			{
				active = false;
				SoundPlayer.getInstance().playSound(SoundPlayer.SOUND_TEST);
			}
			else
			{
				active = true;
				SoundPlayer.getInstance().playSound(SoundPlayer.SOUND_TEST);
			}
		}
	}
	
	public boolean isActive()
	{
		return active;
	}
}
