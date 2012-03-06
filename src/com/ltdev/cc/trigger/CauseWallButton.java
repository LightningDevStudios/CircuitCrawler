package com.ltdev.cc.trigger;

import com.ltdev.cc.entity.WallButton;
import com.ltdev.trigger.Cause;

/**
 * A Cause that depends on the pressed state of a WallButton.
 * @author Lightning Development Studios
 */
public class CauseWallButton extends Cause
{
	private WallButton button;
	
	/**
	 * Initializes a new instance of the CauseWallButton class.
	 * @param button The WallButton to check.
	 */
	public CauseWallButton(WallButton button)
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
