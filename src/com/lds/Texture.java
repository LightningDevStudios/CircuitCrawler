package com.lds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

//In C++ this would be a struct, but alas...
public class Texture 
{
	private int xSize, ySize, xPixels, yPixels, xTiles, yTiles, texturePtr;
	private int minFilter, magFilter, wrapS, wrapT;
	private float offsetX, offsetY;
	private String id;
	private Bitmap bmp;
	
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Context context, String id)
	{
		this(genBitmapFromRawStream(context, texID), xSize, ySize, xTiles, yTiles, id);
	}
	
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
		
		minFilter = GL10.GL_NEAREST;
		magFilter = GL10.GL_NEAREST;
		wrapS = GL10.GL_REPEAT;
		wrapT = GL10.GL_REPEAT;
		
		this.id = id;
	}
	
	public Texture(String text, StringRenderer sr)
	{
		this.bmp = sr.stringToBitmap(text);
		this.xSize = bmp.getWidth();
		this.ySize = bmp.getHeight();
		this.xTiles = 1;
		this.yTiles = 1;
		this.xPixels = xSize;
		this.yPixels = ySize;
		
		minFilter = GL10.GL_NEAREST;
		magFilter = GL10.GL_NEAREST;
		wrapS = GL10.GL_REPEAT;
		wrapT = GL10.GL_REPEAT;
		
		id = text;
	}
	
	public int getXSize()
	{
	    return xSize;
	}
	
	public int getYSize()
	{
	    return ySize;
	}
	
	public int getXPixPerTile()
	{
	    return xPixels;
	}
	
	public int getYPixPerTile()
	{
	    return yPixels;
	}
	
	public int getXTiles()
	{
	    return xTiles;
	}
	
	public int getYTiles()
	{
	    return yTiles;
	}
	
	public int getTexture()
	{
	    return texturePtr;
	}
	
	public int getMinFilter()
	{
	    return minFilter;
	}
	
	public int getMagFilter()
	{
	    return magFilter;
	}
	
	public int getWrapS()
	{
	    return wrapS;
	}
	
	public int getWrapT()
	{
	    return wrapT;
	}
	
	public String getID()
	{
	    return id;
	}
	
	public Bitmap getBitmap()
	{
	    return bmp;
	}
	
	public float getOffsetX()
	{
	    return offsetX;
	}
	
	public float getOffsetY()
	{
	    return offsetY;
	}
	
	public void setTexture(int texturePtr)
	{
	    this.texturePtr = texturePtr;
	}
	
	public void setBitmap(Bitmap bmp)
	{
	    this.bmp = bmp;
	}
	
	public void setMinFilter(int glCap)
	{
	    this.minFilter = glCap;
	}
	
	public void setMagFilter(int glCap)	
	{
	    this.magFilter = glCap;
	}
	
	public void setWrapS(int glCap)
	{
	    this.wrapS = glCap;
	}
	
	public void setWrapT(int glCap)
	{
	    this.wrapT = glCap;
	}
	
	public void setID(String id)
	{
	    this.id = id;
	}
	
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
