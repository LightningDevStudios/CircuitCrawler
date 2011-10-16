package com.lds.trigger;

import com.lds.game.entity.Button;

public class CauseButton extends Cause//cause based on the state of a button
{
	private Button button;
	
	public CauseButton(Button button)
	{
		super();
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
