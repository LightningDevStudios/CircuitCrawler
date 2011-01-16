package com.lds.trigger;

import com.lds.game.Button;

public class ButtonCause extends Cause//cause based on the state of a button
{
	Button button;
	
	public ButtonCause (Button button)
	{
		super();
		this.button = button;
	}
	
	@Override
	public boolean isTriggered ()
	{
		return button.isActive();
	}
}
