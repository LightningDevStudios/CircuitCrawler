package com.lds.trigger;

import com.lds.game.entity.Player;

/**
 * A Cause that triggers when the player's health drops below a specified threshold.
 * @author Lightning Development Studios
 */
public class CausePlayerHealth extends Cause 
{
	private int healthLimit;
	private Player player;
	
	/**
	 * Initializes a new instance of the CausePlayerHealth class.
	 * @param healthLimit The health threshold that will trigger the Cause. Less than or equal to.
	 * @param player The level's player instance.
	 */
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
