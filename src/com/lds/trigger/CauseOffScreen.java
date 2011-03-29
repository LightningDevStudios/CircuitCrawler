package com.lds.trigger;

import com.lds.game.Game;
import com.lds.game.entity.Player;
import com.lds.game.entity.Tile;

public class CauseOffScreen extends Cause
{
	private Player player;
	private Tile[][] tileset;
	
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
