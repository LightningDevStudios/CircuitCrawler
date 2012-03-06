package com.ltdev.cc.trigger;

import com.ltdev.cc.Game;
import com.ltdev.cc.Tile;
import com.ltdev.cc.entity.Player;
import com.ltdev.trigger.Cause;

/**
 * A Cause that triggers when the Player is outside the bounds of the world.
 * @author Robert Rouhani
 * @see Player
 * @deprecated Use a CauseNOT combined with a CauseLocation containing the entire level.
 */
public class CauseOffScreen extends Cause
{
	private Player player;
	private Tile[][] tileset;
	
	/**
	 * Initializes a new instance of the CauseOffScreen class.
	 * @param player The level's player instance.
	 * @param tileset The level's tileset.
	 */
	public CauseOffScreen(Player player, Tile[][] tileset)
	{
		this.player = player;
		this.tileset = tileset;
	}
	
	@Override
	public void update()
	{
		if (Game.nearestTile(player, tileset) == null)
			trigger();
		else
			untrigger();
	}
}
