package com.lds;

import com.lds.game.entity.Tile;

public class TilesetHelper 
{
	private TilesetHelper()
	{
		
	}
	
	public static float[] getTextureVertices(Texture tex, int x, int y)
	{
		return getTextureVertices(x, y, 0, tex.getXTiles() - 1, 0, tex.getYTiles() - 1, tex.getOffsetX(), tex.getOffsetY());
	}
	
	public static float[] getTextureVertices(int x, int y, int minX, int maxX, int minY, int maxY, float offsetX, float offsetY)
	{
		if (x >= minX && x <= maxX && y >= minY && y <= maxY)
		{
			final float intervalX = 1.0f/(float)(maxX - minX + 1);
			final float intervalY = 1.0f/(float)(maxY - minY + 1);
			
			final float negX = x * intervalX + offsetX;
			final float posX = (x + 1) * intervalX - offsetX;
			
			final float negY = y * intervalY + offsetY;
			final float posY = (y + 1) * intervalY - offsetY;
			
			final float[] coords = { 	negX, negY,
										posX, negY,
										negX, posY,
										posX, posY };
			
			return coords;
								
		}
		else 
			return null;
	}
	
	public static float[] getTextureVertices(Texture tex, int tileID)
	{
		final int y = tileID / tex.getXTiles();
		final int x = tileID - (tex.getXTiles() * y);
		
		return getTextureVertices(x, y, 0, tex.getXTiles() - 1, 0, tex.getYTiles() - 1, tex.getOffsetX(), tex.getOffsetY());
	}
	
	public static int getTilesetIndex(float[] vertices, int min, int max)
	{
		final float interval = (1.0f / (float)(max - min + 1));
		
		final int x = (int)(vertices[4] / interval);
		final int y = (int)(vertices[1] / interval);
		
		return (y * (max - min + 1) -+ x);
	}

	public static int getTilesetX(int tileID, Texture tex)
	{
		return tileID - (tex.getXTiles() * (tileID / tex.getXTiles()));
	}
	
	public static int getTilesetY(int tileID, Texture tex)
	{
		return (tileID / tex.getXTiles());
	}
	
	public static int getTilesetID(int x, int y, Texture tex)
	{
		return (y * tex.getXTiles() + x);
	}
	
	public static void setInitialTilesetOffset(Tile[][] tileset)
	{
		final int length = tileset.length, width = tileset[0].length;
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < width; j++)
			{
				tileset[i][j].setXPos((-(float)width / 2 * Tile.TILE_SIZE_F) + (j * Tile.TILE_SIZE_F));
				tileset[i][j].setYPos(((float)length / 2 * Tile.TILE_SIZE_F) - (i * Tile.TILE_SIZE_F));
			}
		}
	}
	
	public static void setInitialTileOffset(Tile tile, int y, int x, int length, int width)
	{
		tile.setXPos((-(float)width / 2.0f * Tile.TILE_SIZE_F) + (x * Tile.TILE_SIZE_F) + (Tile.TILE_SIZE_F / 2));
		tile.setYPos(((float)length / 2.0f * Tile.TILE_SIZE_F) - (y * Tile.TILE_SIZE_F) - (Tile.TILE_SIZE_F / 2));
		
	}
}

