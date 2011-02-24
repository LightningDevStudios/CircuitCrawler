package com.lds.parser;

import java.util.HashMap;

import com.lds.Enums.TileState;
import com.lds.game.Game;
import com.lds.game.entity.Tile;

public class TileData
{
	private int tilePosX, tilePosY;
	private TileState state;
	private Tile tile;
	
	public TileData(HashMap<String, String> tileHM, int x, int y, int tilesetX, int tilesetY)
	{
		this.tilePosX = x;
		this.tilePosY = y;
		
		tile = new Tile(Tile.TILE_SIZE_F, x, y, tilesetY - 1, tilesetX - 1);
		tile.enableTilesetMode(Game.tilesetwire, 0, 0);
		
		if(tileHM.get("tileState").equalsIgnoreCase("floor"))
			tile.setAsFloor();
		else if (tileHM.get("tileState").equalsIgnoreCase("wall"))
			tile.setAsWall();
		else if (tileHM.get("tileState").equalsIgnoreCase("pit"))
			tile.setAsPit();
		else if (tileHM.get("tileState").equalsIgnoreCase("bridge"))
			tile.setAsBridge();
		
	}
	
	public int getTilePosX() 	{return tilePosX;}
	public int getTilePosY()	{return tilePosY;}
	public Tile getTile()		{return tile;}

	public void setTilePosX(int newTilePosX)	{tilePosX = newTilePosX;}
	public void setTilePosY(int newTilePosY)	{tilePosY = newTilePosY;}
}