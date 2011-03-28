package com.lds.trigger;

import com.lds.game.entity.WallButton;

public class CauseWallButton extends Cause//cause based on the state of a button
{
	WallButton button;
	
	public CauseWallButton (WallButton button)
	{
		this.button = button;
	}
	
	@Override
	public void update()
	{
		if (button.isActive())
			trigger();
		
		else
			untrigger();
	}
}
