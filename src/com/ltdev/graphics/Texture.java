package com.ltdev.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

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
	private Bitmap bmp;
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param texID The resource ID of the texture.
	 * @param xSize The size of the texture in the X direction.
	 * @param ySize The size of the texture in the Y direction.
	 * @param xTiles The number of tiles contained in the texture in the X direction.
	 * @param yTiles The number of tiles contained in the texture in the Y direction.
	 * @param context The Android context.
	 * @param gl The OpenGL context.
	 */
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Context context, GL11 gl)
	{
		this(genBitmapFromRawStream(context, texID), xSize, ySize, xTiles, yTiles, gl);
	}
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param bmp The bitmap to read texture data from.
	 * @param xTiles The number of tiles contained in the texture in the X direction.
     * @param yTiles The number of tiles contained in the texture in the Y direction.
	 * @param gl The OpenGL context.
	 */
	public Texture(Bitmap bmp, int xTiles, int yTiles, GL11 gl)
	{
	    this(bmp, bmp.getWidth(), bmp.getHeight(), xTiles, yTiles, gl);
	}
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param bmp The bitmap to read texture data from.
	 * @param xSize The size of the texture in the X direction.
	 * @param ySize The size of the texture in the Y direction.
	 * @param xTiles The number of tiles contained in the texture in the X direction.
	 * @param yTiles The number of tiles contained in the texture in the Y direction.
	 * @param gl The OpenGL context.
	 */
	public Texture(Bitmap bmp, int xSize, int ySize, int xTiles, int yTiles, GL11 gl)
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
		
		//Generate a unique integer that OpenGL stores as a pointer to the texture
        int[] tempTexture = new int[1];
        gl.glGenTextures(1, tempTexture, 0);
        
        texturePtr = tempTexture[0];
        
        //Bind said pointer to the default texture pointer, so it render that texture
        gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
        
        //Parameters for this texture
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
        
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapS);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapT);
        
        //generate the texture from the bitmap.
        GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bmp, 0);
        
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Initializes a new instance of the Texture class.
	 * @param text The text to render
	 * @param sr The StringRenderer used to draw the text.
	 * @param gl The OpenGL context.
	 */
	public Texture(String text, StringRenderer sr, GL11 gl)
	{
	    this(sr.stringToBitmap(text, TextureManager.getTexture("text")), 1, 1, gl);
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
	 * @param gl The OpenGL context.
	 */
	public void setBitmap(Bitmap bmp, GL11 gl)
	{
	    this.bmp = bmp;
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
	    GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bmp, 0);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Sets the texture's minification filter.
	 * @param glCap The new minification filter.
	 * @param gl The OpenGL context.
	 */
	public void setMinFilter(int glCap, GL11 gl)
	{
	    this.minFilter = glCap;
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
	    gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	    
	}
	
	/**
     * Sets the texture's magnification filter.
     * @param glCap The new magnification filter.
     * @param gl The OpenGL context.
     */
	public void setMagFilter(int glCap, GL11 gl)	
	{
	    this.magFilter = glCap;
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Sets the texture's wrap mode in the S direction.
	 * @param glCap The new wrap mode.
	 * @param gl The OpenGL context.
	 */
	public void setWrapS(int glCap, GL11 gl)
	{
	    this.wrapS = glCap;
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapS);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	/**
     * Sets the texture's wrap mode in the T direction.
     * @param glCap The new wrap mode.
     * @param gl The OpenGL context.
     */
	public void setWrapT(int glCap, GL11 gl)
	{
	    this.wrapT = glCap;
	    gl.glBindTexture(GL11.GL_TEXTURE_2D, texturePtr);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapT);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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
