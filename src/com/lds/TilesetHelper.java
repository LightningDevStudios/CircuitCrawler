package com.lds;

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
			
			float[] coords = { posX, negY,
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
}

