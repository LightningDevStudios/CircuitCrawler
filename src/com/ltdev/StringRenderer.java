/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
