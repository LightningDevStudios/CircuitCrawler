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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ltdev.cc.Game;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL11;

/**
 * Wraps an OpenGL texture.
 * @author Lightning Development Studios
 */
public class Texture 
{
	private int xSize, ySize, xPixels, yPixels, xTiles, yTiles, texturePtr;
	private int minFilter, magFilter, wrapS, wrapT;
	private float offsetX, offsetY;
	private String id;
	private Bitmap bmp;
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param texID The resource ID of the texture.
	 * @param xSize The size of the texture in the X direction.
	 * @param ySize The size of the texture in the Y direction.
	 * @param xTiles The number of tiles contained in the texture in the X direction.
	 * @param yTiles The number of tiles contained in the texture in the Y direction.
	 * @param context The Android context.
	 * @param id A string identifier for the texture.
	 */
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Context context, String id)
	{
		this(genBitmapFromRawStream(context, texID), xSize, ySize, xTiles, yTiles, id);
	}
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param bmp The bitmap to read texture data from.
	 * @param xSize The size of the texture in the X direction.
	 * @param ySize The size of the texture in the Y direction.
	 * @param xTiles The number of tiles contained in the texture in the X direction.
	 * @param yTiles The number of tiles contained in the texture in the Y direction.
	 * @param id A string identifier for the texture.
	 */
	public Texture(Bitmap bmp, int xSize, int ySize, int xTiles, int yTiles, String id)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.xTiles = xTiles;
		this.yTiles = yTiles;
		if (xTiles == 0 || yTiles == 0)
		{
			this.xPixels = xSize;
			this.yPixels = ySize;
		}
		else
		{
			this.xPixels = xSize / xTiles;
			this.yPixels = ySize / yTiles;
		}
		this.bmp = bmp;
		
		this.offsetX = 1.0f / (float)(2 * xSize);
		this.offsetY = 1.0f / (float)(2 * ySize);
		
		minFilter = GL11.GL_NEAREST;
		magFilter = GL11.GL_NEAREST;
		wrapS = GL11.GL_CLAMP_TO_EDGE;
        wrapT = GL11.GL_CLAMP_TO_EDGE;
		
		this.id = id;
	}
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param text The text to render
	 * @param sr The StringRenderer used to draw the text.
	 */
	public Texture(String text, StringRenderer sr)
	{
		this.bmp = sr.stringToBitmap(text, Game.text);
		this.xSize = bmp.getWidth();
		this.ySize = bmp.getHeight();
		this.xTiles = 1;
		this.yTiles = 1;
		this.xPixels = xSize;
		this.yPixels = ySize;
		
		minFilter = GL11.GL_NEAREST;
		magFilter = GL11.GL_NEAREST;
		wrapS = GL11.GL_CLAMP_TO_EDGE;
		wrapT = GL11.GL_CLAMP_TO_EDGE;
		
		id = text;
	}
	
	/**
	 * Gets the size of the texture in the X direction.
	 * @return The size of the texture in the X direction.
	 */
	public int getXSize()
	{
	    return xSize;
	}
	
	/**
	 * Gets the size of the texture in the Y direction.
	 * @return The size of the texture in the Y direction.
	 */
	public int getYSize()
	{
	    return ySize;
	}
	
	/**
	 * Gets the number of pixels per tile in the X direction.
	 * @return The number of pixels per tile in the X direction.
	 */
	public int getXPixPerTile()
	{
	    return xPixels;
	}
	
	/**
	 * Gets the number of pixels per tile in the Y direction.
	 * @return The number of pixels per tile in the Y direction.
	 */
	public int getYPixPerTile()
	{
	    return yPixels;
	}
	
	/**
	 * Gets the number of tiles in the X direction.
	 * @return The number of tiles in the X direction.
	 */
	public int getXTiles()
	{
	    return xTiles;
	}
	
	/**
     * Gets the number of tiles in the Y direction.
     * @return The number of tiles in the Y direction.
     */
	public int getYTiles()
	{
	    return yTiles;
	}
	
	/**
	 * Gets the OpenGL texture ID.
	 * @return The texture ID.
	 */
	public int getTexture()
	{
	    return texturePtr;
	}
	
	/**
	 * Gets the texture's minification filter.
	 * @return The texture's minification filter.
	 */
	public int getMinFilter()
	{
	    return minFilter;
	}
	
	/**
     * Gets the texture's magnification filter.
     * @return The texture's magnification filter.
     */
	public int getMagFilter()
	{
	    return magFilter;
	}
	
	/**
	 * Gets the texture's wrap mode in the S direction.
	 * @return The texture's wrap mode in the S direction.
	 */
	public int getWrapS()
	{
	    return wrapS;
	}
	
	/**
     * Gets the texture's wrap mode in the T direction.
     * @return The texture's wrap mode in the T direction.
     */
	public int getWrapT()
	{
	    return wrapT;
	}
	
	/**
	 * Gets the string identifier associated with the texture.
	 * @return The texture's string identifier.
	 */
	public String getID()
	{
	    return id;
	}
	
	/**
	 * Gets the Bitmap that holds the texture data.
	 * @return The texture's bitmap.
	 */
	public Bitmap getBitmap()
	{
	    return bmp;
	}
	
	/**
	 * Gets the texture's "offset", or size of a half-pixel in the X direction.
	 * @return The texture's X offset value.
	 */
	public float getOffsetX()
	{
	    return offsetX;
	}
	
	/**
     * Gets the texture's "offset", or size of a half-pixel in the Y direction.
     * @return The texture's Y offset value.
     */
	public float getOffsetY()
	{
	    return offsetY;
	}
	
	/**
	 * Sets the texture's OpenGL id.
	 * @param texturePtr The new texture ID.
	 */
	public void setTexture(int texturePtr)
	{
	    this.texturePtr = texturePtr;
	}
	
	/**
	 * Sets the bitmap that contains the texture data.
	 * @param bmp The bitmap.
	 */
	public void setBitmap(Bitmap bmp)
	{
	    this.bmp = bmp;
	}
	
	/**
	 * Sets the texture's minification filter.
	 * @param glCap The new minification filter.
	 */
	public void setMinFilter(int glCap)
	{
	    this.minFilter = glCap;
	}
	
	/**
     * Sets the texture's magnification filter.
     * @param glCap The new magnification filter.
     */
	public void setMagFilter(int glCap)	
	{
	    this.magFilter = glCap;
	}
	
	/**
	 * Sets the texture's wrap mode in the S direction.
	 * @param glCap The new wrap mode.
	 */
	public void setWrapS(int glCap)
	{
	    this.wrapS = glCap;
	}
	
	/**
     * Sets the texture's wrap mode in the T direction.
     * @param glCap The new wrap mode.
     */
	public void setWrapT(int glCap)
	{
	    this.wrapT = glCap;
	}
	
	/**
	 * Sets the string identifier associated with this texture.
	 * @param id The new identifier.
	 */
	public void setID(String id)
	{
	    this.id = id;
	}
	
	/**
	 * Generates a Bitmap from a resource.
	 * @param context The Android context.
	 * @param texID The resource ID of the texture.
	 * @return A bitmap containing the resource's image data.
	 */
	private static Bitmap genBitmapFromRawStream(Context context, int texID)
	{
		Bitmap tempBmp;
		InputStream is = context.getResources().openRawResource(texID);
		try
		{
			tempBmp = BitmapFactory.decodeStream(is);
		}
		finally
		{
			try
			{
				is.close();
				is = null;
			}
			
			catch (IOException e)
			{
			    e.printStackTrace();
			}
		}
		return tempBmp;
	}
}
