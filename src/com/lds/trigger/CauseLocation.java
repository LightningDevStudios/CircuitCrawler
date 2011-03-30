package com.lds.trigger;

import com.lds.game.entity.Player;

public class CauseLocation extends Cause
{
	private Player player;
	private float minX, maxX, minY, maxY;
	
	public CauseLocation (Player player, float minX, float maxX, float minY, float maxY)
	{
		this.player = player;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public void update()
	{
		if (player.getXPos() >= minX && player.getXPos() <= maxX && player.getYPos() >= minY && player.getYPos() <= maxY)
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
