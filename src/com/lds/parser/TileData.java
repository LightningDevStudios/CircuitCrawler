package com.lds.parser;

import com.lds.game.entity.Tile;
import com.lds.game.entity.Tile.TileType;

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
        else if (state.equalsIgnoreCase("bridge"))
            tState = TileType.BRIDGE;
				
		tile = new Tile(gl, Tile.TILE_SIZE_F, x, y, tilesetY, tilesetX, tState);
	}
	
	public int getTilePosX()
	{
	    return tilePosX; 
	}
	
	public int getTilePosY()
	{
	    return tilePosY;
	}
	
	public Tile getTile()
	{
	    return tile;
	}

	public void setTilePosX(int newTilePosX)
	{
	    tilePosX = newTilePosX;
	}
	
	public void setTilePosY(int newTilePosY)
	{
	    tilePosY = newTilePosY;
	}
}
