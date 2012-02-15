package com.lds;

import android.graphics.Point;

import com.lds.game.Tile;
import com.lds.math.Vector2;

import java.util.HashMap;

public final class TilesetHelper 
{
    public static HashMap<Byte, Point> pitTexPoints;
    public static HashMap<Byte, Point> wallTexPoints;
        
    static
    {
        pitTexPoints = new HashMap<Byte, Point>();
        wallTexPoints = new HashMap<Byte, Point>();
        
        //pit tiles
        pitTexPoints.put((byte)0xFF, new Point(4, 0));
        pitTexPoints.put((byte)0x29, new Point(5, 0));
        pitTexPoints.put((byte)0x94, new Point(6, 0));
        pitTexPoints.put((byte)0xBF, new Point(7, 0));
        pitTexPoints.put((byte)0x07, new Point(4, 1));
        pitTexPoints.put((byte)0x2F, new Point(5, 1));
        pitTexPoints.put((byte)0x57, new Point(6, 1));
        pitTexPoints.put((byte)0xBD, new Point(7, 1));
        pitTexPoints.put((byte)0xE0, new Point(4, 2));
        pitTexPoints.put((byte)0xE9, new Point(5, 2));
        pitTexPoints.put((byte)0xF4, new Point(6, 2));
        pitTexPoints.put((byte)0xFD, new Point(7, 2));
        pitTexPoints.put((byte)0xEF, new Point(4, 3));
        pitTexPoints.put((byte)0xE7, new Point(5, 3));
        pitTexPoints.put((byte)0xF7, new Point(6, 3));
        pitTexPoints.put((byte)0x00, new Point(7, 3));
        
        //wall tiles
        wallTexPoints.put((byte)0xFF, new Point(0, 4));
        wallTexPoints.put((byte)0x29, new Point(1, 4));
        wallTexPoints.put((byte)0x94, new Point(2, 4));
        wallTexPoints.put((byte)0xBF, new Point(3, 4));
        wallTexPoints.put((byte)0x07, new Point(0, 5));
        wallTexPoints.put((byte)0x2F, new Point(1, 5));
        wallTexPoints.put((byte)0x57, new Point(2, 5));
        wallTexPoints.put((byte)0xBD, new Point(3, 5));
        wallTexPoints.put((byte)0xE0, new Point(0, 6));
        wallTexPoints.put((byte)0xE9, new Point(1, 6));
        wallTexPoints.put((byte)0xF4, new Point(2, 6));
        wallTexPoints.put((byte)0xFD, new Point(3, 6));
        wallTexPoints.put((byte)0xEF, new Point(0, 7));
        wallTexPoints.put((byte)0xE7, new Point(1, 7));
        wallTexPoints.put((byte)0xF7, new Point(2, 7));
        wallTexPoints.put((byte)0x00, new Point(3, 7));
        
        wallTexPoints.put((byte)0x80, new Point(4, 4));
        wallTexPoints.put((byte)0x20, new Point(5, 4));
        wallTexPoints.put((byte)0xA4, new Point(6, 4));
        wallTexPoints.put((byte)0xA1, new Point(7, 4));
        wallTexPoints.put((byte)0x04, new Point(4, 5));
        wallTexPoints.put((byte)0x01, new Point(5, 5));
        wallTexPoints.put((byte)0x85, new Point(6, 5));
        wallTexPoints.put((byte)0x25, new Point(7, 5));
        wallTexPoints.put((byte)0x84, new Point(4, 6));
        wallTexPoints.put((byte)0x21, new Point(5, 6));
        wallTexPoints.put((byte)0xA5, new Point(6, 6));
        wallTexPoints.put((byte)0xA9, new Point(7, 6));
        wallTexPoints.put((byte)0xA0, new Point(4, 7));
        wallTexPoints.put((byte)0x05, new Point(5, 7));
        wallTexPoints.put((byte)0xAD, new Point(6, 7));
        wallTexPoints.put((byte)0x2D, new Point(7, 7));
        
        wallTexPoints.put((byte)0xAF, new Point(8,  4));
        wallTexPoints.put((byte)0xB7, new Point(9,  4));
        wallTexPoints.put((byte)0x87, new Point(10, 4));
        wallTexPoints.put((byte)0xED, new Point(8,  5));
        wallTexPoints.put((byte)0xF5, new Point(9,  5));
        wallTexPoints.put((byte)0xA7, new Point(10, 5));
        wallTexPoints.put((byte)0x81, new Point(11, 5));
        wallTexPoints.put((byte)0xB4, new Point(8,  6));
        wallTexPoints.put((byte)0xE5, new Point(9,  6));
        wallTexPoints.put((byte)0xE1, new Point(10, 6));
        wallTexPoints.put((byte)0x24, new Point(11, 6));
        wallTexPoints.put((byte)0x95, new Point(8,  7));
        wallTexPoints.put((byte)0xB5, new Point(9,  7));
        wallTexPoints.put((byte)0xE4, new Point(10, 7));
        wallTexPoints.put((byte)0x27, new Point(11, 7));
    }
    
	private TilesetHelper()
	{
		
	}

	/**
	 * Gets the vertices for a wall tile.
	 * @param borders The borders of the wall.
	 * @param size The size of the tile.
	 * @return The vertices for the wall tile.
	 */
	public static float[] getWallVertices(byte borders, float size)
	{
	    boolean left = (borders & 0x08) == 0x08;
	    boolean right = (borders & 0x10) == 0x10;
	    boolean top = (borders & 0x02) == 0x02;
	    boolean bottom = (borders & 0x40) == 0x40;
	    
	    int vertLength = 12;
	    if (left) vertLength += 12;
	    if (right) vertLength += 12;
	    if (bottom) vertLength += 12;
	    if (top) vertLength += 12;
	    
	    float[] vertices = new float[vertLength];
	    
	    vertices[0] = -size;
	    vertices[1] = size;
	    vertices[2] = 0.175f;
	    vertices[3] = -size;
	    vertices[4] = -size;
	    vertices[5] = 0.175f;
	    vertices[6] = size;
	    vertices[7] = -size;
	    vertices[8] = 0.175f;
	    vertices[9] = size;
	    vertices[10] = size;
	    vertices[11] = 0.175f;
	    
	    int addPosition = 12;
	    
	    if (left)
	    {
	        vertices[addPosition] = -size;
	        vertices[addPosition + 1] = size;
	        vertices[addPosition + 2] = 0.175f;
	        vertices[addPosition + 3] = -size;
	        vertices[addPosition + 4] = size;
	        vertices[addPosition + 5] = 0f;
	        vertices[addPosition + 6] = -size;
	        vertices[addPosition + 7] = -size;
	        vertices[addPosition + 8] = 0f;
	        vertices[addPosition + 9] = -size;
	        vertices[addPosition + 10] = -size;
	        vertices[addPosition + 11] = 0.175f;
	        addPosition += 12;
	    }
	    
	    if (right)
	    {
	        vertices[addPosition] = size;
            vertices[addPosition + 1] = -size;
            vertices[addPosition + 2] = 0.175f;
            vertices[addPosition + 3] = size;
            vertices[addPosition + 4] = -size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = size;
            vertices[addPosition + 7] = size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = size;
            vertices[addPosition + 10] = size;
            vertices[addPosition + 11] = 0.175f;
            addPosition += 12;
	    }
	    
	    if (top)
	    {
	        vertices[addPosition] = size;
            vertices[addPosition + 1] = size;
            vertices[addPosition + 2] = 0.175f;
            vertices[addPosition + 3] = size;
            vertices[addPosition + 4] = size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = -size;
            vertices[addPosition + 7] = size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = -size;
            vertices[addPosition + 10] = size;
            vertices[addPosition + 11] = 0.175f;
            addPosition += 12;
	    }
	    
	    if (bottom)
	    {
	        vertices[addPosition] = -size;
            vertices[addPosition + 1] = -size;
            vertices[addPosition + 2] = 0.175f;
            vertices[addPosition + 3] = -size;
            vertices[addPosition + 4] = -size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = size;
            vertices[addPosition + 7] = -size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = size;
            vertices[addPosition + 10] = -size;
            vertices[addPosition + 11] = 0.175f;
            addPosition += 12;
	    }
	    
	    return vertices;
	}
	
	public static float[] getWallTextureVertices(byte borders)
	{
	    boolean left = (borders & 0x08) == 0x08;
        boolean right = (borders & 0x10) == 0x10;
        boolean top = (borders & 0x02) == 0x02;
        boolean bottom = (borders & 0x40) == 0x40;
        
        int sideCount = 0;
        if (left) sideCount++;
        if (right) sideCount++;
        if (bottom) sideCount++;
        if (top) sideCount++;
        
        float[] texCoords = new float[(sideCount + 1) * 8];
        
        float[] baseTexCoords = new float[]
        {
            128f / 512f + 1f / 1024f, 64.0f / 256.0f + 1f / 512f,
            128f / 512f + 1f / 1024f, 128f / 256f - 1f / 512f,
            192f / 512f - 1f / 1024f, 128.0f / 256.0f - 1f / 512f,
            192f / 512f - 1f / 1024f, 64.0f / 256.0f + 1f / 512f,            
        };
        
        float[] sideTexCoords = new float[]
        {
            0, 64f / 256f + 1f / 512f,
            0, 128f / 256f - 1f / 512f,
            64f / 512f - 1f / 1024f, 128f / 256f - 1f / 512f,
            64f / 512f - 1f / 1024f, 64f / 256f + 1f / 512f
        };
        
        System.arraycopy(baseTexCoords, 0, texCoords, 0, baseTexCoords.length);
      
        for (int i = 0; i < sideCount; i++)
        {
            System.arraycopy(sideTexCoords, 0, texCoords, (i + 1) * 8, sideTexCoords.length); 
        }
        
        return texCoords;
	}
	
	public static int[] getWallIndices(byte borders)
	{
	    boolean left = (borders & 0x08) == 0x08;
        boolean right = (borders & 0x10) == 0x10;
        boolean top = (borders & 0x02) == 0x02;
        boolean bottom = (borders & 0x40) == 0x40;
        
        int sideCount = 0;
        if (left) sideCount++;
        if (right) sideCount++;
        if (bottom) sideCount++;
        if (top) sideCount++;
        
        int[] indices = new int[(sideCount + 1) * 6];
        
        int[] trianglesIndices = new int[] { 0, 1, 2, 0, 2, 3 };
        
        System.arraycopy(trianglesIndices, 0, indices, 0, trianglesIndices.length);
        
        for (int i = 0; i < sideCount; i++)
        {
            int[] newIndices = new int[6];
            for (int j = 0; j < trianglesIndices.length; j++)
            {
                newIndices[j] = trianglesIndices[j] + ((i + 1) * 4);
            }
            
            System.arraycopy(newIndices, 0, indices, (i + 1) * 6, newIndices.length); 
        }
        
        return indices;
	}
	
	public static float[] getTextureVertices(Texture tex, Point p)
	{
		return getTextureVertices(p.x, p.y, 0, tex.getXTiles() - 1, 0, tex.getYTiles() - 1, tex.getOffsetX(), tex.getOffsetY());
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
			
			/*final float[] coords = 
		    {
		        negX, negY,
				posX, negY,
				negX, posY,
				posX, posY 
		    };*/
			
			final float[] coords = 
            {
                negX, negY,
                negX, posY,
                posX, negY,
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
	
	public static void setInitialTileOffset(Tile tile, Point p, int length, int width)
	{
	    tile.setPos(new Vector2((-(float)width / 2.0f * Tile.TILE_SIZE_F) + (p.x * Tile.TILE_SIZE_F) + (Tile.TILE_SIZE_F / 2), 
	            ((float)length / 2.0f * Tile.TILE_SIZE_F) - (p.y * Tile.TILE_SIZE_F) - (Tile.TILE_SIZE_F / 2)));
		
	}
}

