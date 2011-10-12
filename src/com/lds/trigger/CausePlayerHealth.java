package com.lds.trigger;

import com.lds.game.entity.Player;

public class CausePlayerHealth extends Cause 
{
	private int healthLimit;
	private Player player;
	
	public CausePlayerHealth(int healthLimit, Player player)
	{
		super();
		this.healthLimit = healthLimit;
		this.player = player;
	}
	
	@Override
	public void update()
	{
		if (player.getHealth() <= healthLimit)
			trigger();
	}
}
