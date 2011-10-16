package com.lds.trigger;

import com.lds.game.entity.Button;

/**
 * A Cause that depends on the pressed state of a button.
 * @author Lightning Development Studios
 * @see Button
 */
public class CauseButton extends Cause
{
	private Button button;
	
	/**
	 * Initializes a new instance of the CauseButton class.
	 * @param button The button to check the pressed state of.
	 */
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
