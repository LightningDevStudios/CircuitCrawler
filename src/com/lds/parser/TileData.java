package com.lds.parser;

import java.util.HashMap;

import com.lds.Enums.AIType;
import com.lds.Enums.TileState;

public class TileData //extends StaticEntData
{
	private int tilePosX, tilePosY, tilesetX, tilesetY;
	private TileState state;
	
	public TileData(HashMap<String, String> tileHM, int x, int y, int tilesetX, int tilesetY)
	{
		//super(tileHM);
		
		/*tilePosX = Integer.parseInt(tileHM.get("tilePosX"));
		tilePosY = Integer.parseInt(tileHM.get("tilePosY"));
		tilesetX = Integer.parseInt(tileHM.get("tilesetX"));
		tilesetY = Integer.parseInt(tileHM.get("tilesetY"));*/
		this.tilePosX = x;
		this.tilePosY = y;
		this.tilesetX = tilesetX;
		this.tilesetY = tilesetY;
		
		if(tileHM.get("tileState").equalsIgnoreCase("floor"))
			state = TileState.FLOOR;
		else if (tileHM.get("tileState").equalsIgnoreCase("wall"))
			state = TileState.WALL;
		else if (tileHM.get("tileState").equalsIgnoreCase("pit"))
			state = TileState.PIT;
		else if (tileHM.get("tileState").equalsIgnoreCase("bridge"))
			state = TileState.BRIDGE;
	}
	
	public int getTilePosX() 	{return tilePosX;}
	public int getTilePosY()	{return tilePosY;}
	public int getTilesetX()	{return tilesetX;}
	public int getTilesetY()	{return tilesetY;}
	
	public void setTilePosX(int newTilePosX)	{tilePosX = newTilePosX;}
	public void setTilePosY(int newTilePosY)	{tilePosY = newTilePosY;}
	public void setTilesetX(int newTilesetX)	{tilesetX = newTilesetX;}
	public void setTilesetY(int newTilesetY)	{tilesetY = newTilesetY;}
}