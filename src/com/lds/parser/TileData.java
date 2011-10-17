package com.lds.parser;

import javax.microedition.khronos.opengles.GL11;

import com.lds.Enums.TileState;
import com.lds.game.entity.Tile;

public class TileData
{
	private int tilePosX, tilePosY;
	private Tile tile;
	
	public TileData(GL11 gl, String state, int x, int y, int tilesetX, int tilesetY)
	{
		this.tilePosX = x;
		this.tilePosY = y;
		
		TileState tState = TileState.FLOOR;
		
		if (state.equalsIgnoreCase("floor"))
            tState = TileState.FLOOR;
        else if (state.equalsIgnoreCase("wall"))
            tState = TileState.WALL;
        else if (state.equalsIgnoreCase("pit"))
            tState = TileState.PIT;
        else if (state.equalsIgnoreCase("bridge"))
            tState = TileState.BRIDGE;
				
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
