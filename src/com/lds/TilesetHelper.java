package com.lds;

import java.util.HashMap;

import android.graphics.Point;

import com.lds.game.entity.Tile;
import com.lds.math.Vector2;

public final class TilesetHelper 
{
    public static HashMap<Byte, Point> pitTexPoints;
    public static HashMap<Byte, Point> wallTexPoints;
    
    static
    {
        pitTexPoints = new HashMap<Byte, Point>();
        wallTexPoints = new HashMap<Byte, Point>();
        
        //blank
        pitTexPoints.put((byte)0x00, new Point(7, 3));
        
        //only 1 edge
        pitTexPoints.put((byte)0x02, new Point(4, 1));
        pitTexPoints.put((byte)0x08, new Point(5, 0));
        pitTexPoints.put((byte)0x10, new Point(6, 0));
        pitTexPoints.put((byte)0x14, new Point(4, 2));
        
        //corners
        pitTexPoints.put((byte)0x0A, new Point(5, 1));
        pitTexPoints.put((byte)0x12, new Point(6, 1));
        pitTexPoints.put((byte)0x48, new Point(5, 2));
        pitTexPoints.put((byte)0x50, new Point(6, 2));
        
        //opposite edges
        pitTexPoints.put((byte)0x18, new Point(7, 1));
        pitTexPoints.put((byte)0x42, new Point(5, 3));
        
        //3 edges
        pitTexPoints.put((byte)0x4A, new Point(4, 3));
        pitTexPoints.put((byte)0x52, new Point(6, 3));
        pitTexPoints.put((byte)0x1A, new Point(7, 0));
        pitTexPoints.put((byte)0x58, new Point(7, 2));
        
        //all 4
        pitTexPoints.put((byte)0x5A, new Point(4, 0));
        
        //blank
        wallTexPoints.put((byte)0x00, new Point(3, 7));
        
        //only 1 edge
        wallTexPoints.put((byte)0x02, new Point(0, 5));
        wallTexPoints.put((byte)0x08, new Point(1, 4));
        wallTexPoints.put((byte)0x10, new Point(2, 4));
        wallTexPoints.put((byte)0x14, new Point(0, 6));
        
        //corners
        wallTexPoints.put((byte)0x0A, new Point(1, 5));
        wallTexPoints.put((byte)0x12, new Point(2, 5));
        wallTexPoints.put((byte)0x48, new Point(1, 6));
        wallTexPoints.put((byte)0x50, new Point(2, 6));
        
        //opposite edges
        wallTexPoints.put((byte)0x18, new Point(3, 5));
        wallTexPoints.put((byte)0x42, new Point(1, 7));
        
        //3 edges
        wallTexPoints.put((byte)0x4A, new Point(0, 7));
        wallTexPoints.put((byte)0x52, new Point(2, 7));
        wallTexPoints.put((byte)0x1A, new Point(3, 4));
        wallTexPoints.put((byte)0x58, new Point(3, 6));
        
        //all 4
        wallTexPoints.put((byte)0x5A, new Point(0, 4));
    }
    
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
			final float intervalX = 1.0f / (float)(maxX - minX + 1);
			final float intervalY = 1.0f / (float)(maxY - minY + 1);
			
			final float negX = x * intervalX + offsetX;
			final float posX = (x + 1) * intervalX - offsetX;
			
			final float negY = y * intervalY + offsetY;
			final float posY = (y + 1) * intervalY - offsetY;
			
			final float[] coords = 
		    {
		        negX, negY,
				posX, negY,
				negX, posY,
				posX, posY 
		    };
			
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
		final float interval = 1.0f / (float)(max - min + 1);
		
		final int x = (int)(vertices[4] / interval);
		final int y = (int)(vertices[1] / interval);
		
		return y * (max - min + 1) - x;
	}

	public static int getTilesetX(int tileID, Texture tex)
	{
		return tileID - (tex.getXTiles() * (tileID / tex.getXTiles()));
	}
	
	public static int getTilesetY(int tileID, Texture tex)
	{
		return tileID / tex.getXTiles();
	}
	
	public static int getTilesetID(int x, int y, Texture tex)
	{
		return y * tex.getXTiles() + x;
	}
	
	public static void setInitialTilesetOffset(Tile[][] tileset)
	{
		final int length = tileset.length, width = tileset[0].length;
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < width; j++)
			{
			    tileset[i][j].setPos(new Vector2((-(float)width / 2 * Tile.TILE_SIZE_F) + (j * Tile.TILE_SIZE_F), ((float)length / 2 * Tile.TILE_SIZE_F) - (i * Tile.TILE_SIZE_F)));
			}
		}
	}
	
	public static void setInitialTileOffset(Tile tile, int y, int x, int length, int width)
	{
	    tile.setPos(new Vector2((-(float)width / 2.0f * Tile.TILE_SIZE_F) + (x * Tile.TILE_SIZE_F) + (Tile.TILE_SIZE_F / 2), 
	            ((float)length / 2.0f * Tile.TILE_SIZE_F) - (y * Tile.TILE_SIZE_F) - (Tile.TILE_SIZE_F / 2)));
		
	}
}

