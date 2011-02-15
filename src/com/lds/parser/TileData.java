package com.lds.parser;

import java.util.HashMap;

public class TileData extends StaticEntData
{
	private int tilePosX, tilePosY, tilesetX, tilesetY;
	
		public TileData(HashMap<String, String> tileHM)
		{
			super(tileHM);
			
			tilePosX = Integer.parseInt(tileHM.get("tilePosX"));
			tilePosY = Integer.parseInt(tileHM.get("tilePosY"));
			tilesetX = Integer.parseInt(tileHM.get("tilesetX"));
			tilesetY = Integer.parseInt(tileHM.get("tilesetY"));
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