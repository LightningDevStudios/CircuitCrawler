package com.lds.trigger;

import com.lds.game.entity.Player;

/**
 * A Cause that is triggered when the player is within a location.
 * @author Lighting Development Studios
 * \todo Allow for any Entity, not just the player.
 */
public class CauseLocation extends Cause
{
	private Player player;
	private float minX, maxX, minY, maxY;
	
	/**
	 * Initializes a new instance of the CauseLocation class.
	 * @param player The level's player instance.
	 * @param minX The location minimum on the X axis.
	 * @param maxX The location maximum on the X axis.
	 * @param minY The location minimum on the Y axis.
	 * @param maxY The location maximum on the Y axis.
	 */
	public CauseLocation(Player player, float minX, float maxX, float minY, float maxY)
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
		if (player.getPos().x() >= minX
		 && player.getPos().x() <= maxX 
		 && player.getPos().y() >= minY 
		 && player.getPos().y() <= maxY)
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
