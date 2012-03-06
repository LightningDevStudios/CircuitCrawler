package com.lds;

import android.graphics.Point;

import com.lds.game.Tile;
import com.lds.math.Vector2;

/**
 * A class that helps with tilesets and texture coordinates.
 * @author Lightning Development Studios
 */
public final class TilesetHelper 
{
    /**
     * Prevents initialization of this static class.
     */
	private TilesetHelper()
	{
		
	}

	/**
	 * Gets the vertices for a non-floor tile.
	 * @param borders The borders of the tile.
	 * @param size The size of the tile.
	 * @param height The height of the tile.
	 * @return The vertices for the wall tile.
	 */
	public static float[] getTileVertices(byte borders, float size, float height)
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
	    vertices[2] = height;
	    vertices[3] = -size;
	    vertices[4] = -size;
	    vertices[5] = height;
	    vertices[6] = size;
	    vertices[7] = -size;
	    vertices[8] = height;
	    vertices[9] = size;
	    vertices[10] = size;
	    vertices[11] = height;
	    
	    int addPosition = 12;
	    
	    if (left)
	    {
	        vertices[addPosition] = -size;
	        vertices[addPosition + 1] = size;
	        vertices[addPosition + 2] = height;
	        vertices[addPosition + 3] = -size;
	        vertices[addPosition + 4] = size;
	        vertices[addPosition + 5] = 0f;
	        vertices[addPosition + 6] = -size;
	        vertices[addPosition + 7] = -size;
	        vertices[addPosition + 8] = 0f;
	        vertices[addPosition + 9] = -size;
	        vertices[addPosition + 10] = -size;
	        vertices[addPosition + 11] = height;
	        addPosition += 12;
	    }
	    
	    if (right)
	    {
	        vertices[addPosition] = size;
            vertices[addPosition + 1] = -size;
            vertices[addPosition + 2] = height;
            vertices[addPosition + 3] = size;
            vertices[addPosition + 4] = -size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = size;
            vertices[addPosition + 7] = size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = size;
            vertices[addPosition + 10] = size;
            vertices[addPosition + 11] = height;
            addPosition += 12;
	    }
	    
	    if (top)
	    {
	        vertices[addPosition] = size;
            vertices[addPosition + 1] = size;
            vertices[addPosition + 2] = height;
            vertices[addPosition + 3] = size;
            vertices[addPosition + 4] = size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = -size;
            vertices[addPosition + 7] = size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = -size;
            vertices[addPosition + 10] = size;
            vertices[addPosition + 11] = height;
            addPosition += 12;
	    }
	    
	    if (bottom)
	    {
	        vertices[addPosition] = -size;
            vertices[addPosition + 1] = -size;
            vertices[addPosition + 2] = height;
            vertices[addPosition + 3] = -size;
            vertices[addPosition + 4] = -size;
            vertices[addPosition + 5] = 0f;
            vertices[addPosition + 6] = size;
            vertices[addPosition + 7] = -size;
            vertices[addPosition + 8] = 0f;
            vertices[addPosition + 9] = size;
            vertices[addPosition + 10] = -size;
            vertices[addPosition + 11] = height;
            addPosition += 12;
	    }
	    
	    return vertices;
	}
	
	/**
	 * Gets the texture coordinates for a wall tile.
	 * @param borders A byte representing the tiles around the tile.
	 * @return The texture coordinates as a float array.
	 */
	public static float[] getWallTexCoords(byte borders)
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
            128f / 512f + 1f / 1024f, 64f / 256f + 1f / 512f,
            128f / 512f + 1f / 1024f, 128f / 256f - 1f / 512f,
            192f / 512f - 1f / 1024f, 128f / 256f - 1f / 512f,
            192f / 512f - 1f / 1024f, 64f / 256f + 1f / 512f,            
        };
        
        float[] sideTexCoords = new float[]
        {
            1f / 1024f, 64f / 256f + 1f / 512f,
            1f / 1024f, 128f / 256f - 1f / 512f,
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
	
	/**
	 * Gets the texture coordinates for a pit tile.
	 * @param borders A byte representing the tiles around the tile.
	 * @return The texture coordinates as a float array.
	 */
	public static float[] getPitTexCoords(byte borders)
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
            128f / 512f + 1f / 1024f, 1f / 512f,
            128f / 512f + 1f / 1024f, 64f / 256f - 1f / 512f,
            192f / 512f - 1f / 1024f, 64f / 256f - 1f / 512f,
            192f / 512f - 1f / 1024f, 1f / 512f,            
        };
        
        float[] sideTexCoords = new float[]
        {
            64f / 512f + 1f / 1024f, 128f / 256f - 1f / 512f,
            64f / 512f + 1f / 1024f, 64f / 256f + 1f / 512f,
            128f / 512f - 1f / 1024f, 64f / 256f + 1f / 512f,
            128f / 512f - 1f / 1024f, 128f / 256f - 1f / 512f
        };
        
        System.arraycopy(baseTexCoords, 0, texCoords, 0, baseTexCoords.length);
      
        for (int i = 0; i < sideCount; i++)
        {
            System.arraycopy(sideTexCoords, 0, texCoords, (i + 1) * 8, sideTexCoords.length); 
        }
        
        return texCoords;
	}
	
	/**
	 * Get the vertex normals for a tile.
	 * @param borders A byte representing the tiles around the tile.
	 * @return The normals as a float array.
	 */
	public static float[] getTileNormals(byte borders)
	{
	    float[] baseNormals =
        {
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
        };
	    
	    boolean left = (borders & 0x08) == 0x08;
        boolean right = (borders & 0x10) == 0x10;
        boolean top = (borders & 0x02) == 0x02;
        boolean bottom = (borders & 0x40) == 0x40;
        
        int sideCount = 0;
        if (left) sideCount++;
        if (right) sideCount++;
        if (bottom) sideCount++;
        if (top) sideCount++;
        
        float[] normals = new float[12 * (sideCount + 1)];
        
        System.arraycopy(baseNormals, 0, normals, 0, baseNormals.length);
        
        int addPosition = 12;
        
        if (left)
        {
            normals[addPosition] = -1;
            normals[addPosition + 3] = -1;
            normals[addPosition + 6] = -1;
            normals[addPosition + 9] = -1;
            addPosition += 12;
        }
        
        if (right)
        {
            normals[addPosition] = 1;
            normals[addPosition + 3] = 1;
            normals[addPosition + 6] = 1;
            normals[addPosition + 9] = 1;
            addPosition += 12;
        }
        
        if (top)
        {
            normals[addPosition + 1] = 1;
            normals[addPosition + 4] = 1;
            normals[addPosition + 7] = 1;
            normals[addPosition + 10] = 1;
            addPosition += 12;
        }
        
        if (bottom)
        {
            normals[addPosition + 1] = -1;
            normals[addPosition + 4] = -1;
            normals[addPosition + 7] = -1;
            normals[addPosition + 10] = -1;
            addPosition += 12;
        }
        
        return normals;
	}
	
	/**
	 * Gets the indices of the tile.
	 * @param borders A byte representing the tiles around the tile.
	 * @return The indices as an int array.
	 */
	public static int[] getTileIndices(byte borders)
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
	
	public static void setInitialTileOffset(Tile tile, Point p, int length, int width)
	{
	    tile.setPos(new Vector2((-(float)width / 2.0f * Tile.TILE_SIZE_F) + (p.x * Tile.TILE_SIZE_F) + (Tile.TILE_SIZE_F / 2), 
	            ((float)length / 2.0f * Tile.TILE_SIZE_F) - (p.y * Tile.TILE_SIZE_F) - (Tile.TILE_SIZE_F / 2)));
		
	}
}

