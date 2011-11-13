package com.lds.parser;

import android.graphics.Point;

import com.lds.game.Tile;
import com.lds.game.Tile.TileType;

import javax.microedition.khronos.opengles.GL11;

public class TileData
{
	private int tilePosX, tilePosY;
	private Tile tile;
	
	public TileData(GL11 gl, String state, int x, int y, int tilesetX, int tilesetY)
	{
		this.tilePosX = x;
		this.tilePosY = y;
		
		TileType tState = TileType.FLOOR;
		
		if (state.equalsIgnoreCase("floor"))
            tState = TileType.FLOOR;
        else if (state.equalsIgnoreCase("wall"))
            tState = TileType.WALL;
        else if (state.equalsIgnoreCase("pit"))
            tState = TileType.PIT;
				
		tile = new Tile(new Point(tilePosX, tilePosY), tilesetY, tilesetX, tState);
	}
	
	/**
	 * Gets the contained Tile instance.
	 * @return An instance of the tile.
	 */
	public Tile getTile()
	{
	    return tile;
	}
}
