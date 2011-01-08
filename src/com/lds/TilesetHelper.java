package com.lds;

import com.lds.game.Tile;

//TODO grab min/max through a textureID type thing
public class TilesetHelper 
{
	private TilesetHelper()
	{
	}
	
	public static float[] getTextureVertices(int x, int y, int min, int max)
	{
		if (x >= min && x <= max && y >= min && y <= max)
		{
			float interval = 1.0f/(float)(max - min + 1);
			
			float negX = x * interval;
			float posX = (x + 1) * interval;
			
			float negY = y * interval;
			float posY = (y + 1) * interval;
			
			float[] coords = { 	posX, negY,
								posX, posY,
								negX, negY,
								negX, posY };
			/*float[] coords = { negX, negY,
								posX, negY,
								negX, posY,
								posX, posY };*/
			return coords;
								
		}
		else 
			return null;
	}
	
	public static float[] getTextureVertices(int tileID)
	{
		int y = tileID / 8;
		int x = tileID - (8 * y);
		
		return getTextureVertices(x, y, 0, 7);
	}
	
	public static int getTilesetIndex(float[] vertices, int min, int max)
	{
		float interval = (1.0f / (float)(max - min + 1));
		
		int x = (int)(vertices[4] / interval);
		int y = (int)(vertices[1] / interval);
		
		return (y * (max - min + 1) + x);
	}
	
	public static int getTilesetX(float[] vertices, int min, int max)
	{
		return (int)(vertices[4] / (1.0f / (float)(min - max + 1)));
	}
	
	public static int getTilesetY(float[] vertices, int min, int max)
	{
		return (int)(vertices[1] / (1.0f / (float)(min - max + 1)));
	}
	
	public static void setInitialTilesetOffset(Tile[][] tileset)
	{
		int length = tileset.length, width = tileset[0].length;
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < width; j++)
			{
				tileset[i][j].xPos = (-(float)width / 2 * Tile.TILE_SIZE_F) + (j * Tile.TILE_SIZE_F);
				tileset[i][j].yPos = ((float)length / 2 * Tile.TILE_SIZE_F) - (i * Tile.TILE_SIZE_F);
			}
		}
	}
	
	public static void setInitialTileOffset(Tile tile, int y, int x, int length, int width)
	{
		tile.xPos = (-(float)width / 2 * Tile.TILE_SIZE_F) + (x * Tile.TILE_SIZE_F);
		tile.yPos = ((float)length / 2 * Tile.TILE_SIZE_F) - (y * Tile.TILE_SIZE_F);
	}
}

