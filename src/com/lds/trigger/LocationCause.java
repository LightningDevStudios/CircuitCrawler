package com.lds.trigger;

import com.lds.game.Player;

public class LocationCause extends Cause
{
	private Player player;
	private int xMin, xMax, yMin, yMax;
	
	public LocationCause (Player player, int xMin, int xMax, int yMin, int yMax)
	{
		this.player = player;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
	
	@Override
	public boolean isTriggered ()
	{
		if (player.getXPos() >= xMin && player.getXPos() <= xMax && player.getYPos() >= yMin && player.getYPos() <= yMax)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
