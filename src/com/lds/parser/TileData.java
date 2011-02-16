package com.lds.parser;

import java.util.HashMap;

import com.lds.Enums.TileState;

public class TileData
{
	private int tilePosX, tilePosY;
	private TileState state;
	
	public TileData(HashMap<String, String> tileHM, int x, int y)
	{
		//super(tileHM);
		
		/*tilePosX = Integer.parseInt(tileHM.get("tilePosX"));
		tilePzosY = Integer.parseInt(tileHM.get("tilePosY"));
		tilesetX = Integer.parseInt(tileHM.get("tilesetX"));
		tilesetY = Integer.parseInt(tileHM.get("tilesetY"));*/
		this.tilePosX = x;
		this.tilePosY = y;
		
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

	public void setTilePosX(int newTilePosX)	{tilePosX = newTilePosX;}
	public void setTilePosY(int newTilePosY)	{tilePosY = newTilePosY;}
}