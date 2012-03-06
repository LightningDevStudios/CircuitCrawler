package com.ltdev;

import android.graphics.Bitmap;

/**
 * Renders text.
 * @author Lightning Development Studios
 */
public final class StringRenderer 
{
	/**
	 * Prevents initialization of StringRenderer.
	 */
	private StringRenderer()
	{
	}
	
	/**
	 * Draws a bitmap from an inputted string.
	 * @param input The string to render.
	 * @param text The fontsheet.
	 * @return A Bitmap containing the text.
	 */
	public Bitmap stringToBitmap(String input, Texture text)
	{
		//Create a new, blank bitmap. Also allocate an int array which the Bitmap class uses to store in getPixels() and read in setPixels()
		//\todo check for ySize with the number of \n chars
		Bitmap textTileset = text.getBitmap();
		int xTileSize = text.getXPixPerTile();
		int yTileSize = text.getYPixPerTile();
		
		Bitmap bmp = Bitmap.createBitmap(input.length() * xTileSize, yTileSize, Bitmap.Config.ARGB_8888);
		int[] charPixels = new int[xTileSize * yTileSize * 4];
		
		//Go through each character, grab the proper pixels from R.drawable.text, by using the native ASCII conversion, String.charAt().
		//Then write those referenced pixels back to the text bitmap
		for (int i = 0; i < input.length(); i++)
		{
			textTileset.getPixels(charPixels, 0, xTileSize, TilesetHelper.getTilesetX((int)input.charAt(i), text) * xTileSize, TilesetHelper.getTilesetY((int)input.charAt(i), text) * yTileSize, xTileSize, yTileSize);
			bmp.setPixels(charPixels, 0, xTileSize, xTileSize * i, 0, xTileSize, yTileSize);
		}
		
		return bmp;
	}
}
