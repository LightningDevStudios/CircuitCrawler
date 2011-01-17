package com.lds;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//In C++ this would be a struct, but alas...
public class Texture 
{
	
	//TODO find a method to store GL caps for texture params
	private int id, xSize, ySize, xPixels, yPixels, xTiles, yTiles, texturePtr;
	private int minFilter, magFilter, wrapS, wrapT;
	private Bitmap bmp;
	
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Context context)
	{
		this(BitmapFactory.decodeResource(context.getResources(), texID), xSize, ySize, xTiles, yTiles);
		this.id = texID;
	}
	
	public Texture(Bitmap bmp, int xSize, int ySize, int xTiles, int yTiles)
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
		
		minFilter = GL10.GL_NEAREST;
		magFilter = GL10.GL_NEAREST;
		wrapS = GL10.GL_REPEAT;
		wrapT = GL10.GL_REPEAT;
	}
	
	public int getTextureID()	{ return id; }
	public int getXSize()		{ return xSize; }
	public int getYSize()		{ return ySize; }
	public int getXPixPerTile()	{ return xPixels; }
	public int getYPixPerTile()	{ return yPixels; }
	public int getXTiles()		{ return xTiles; }
	public int getYTiles()		{ return yTiles; }
	public int getTexture()		{ return texturePtr; }
	public int getMinFilter()	{ return minFilter; }
	public int getMagFilter()	{ return magFilter; }
	public int getWrapS()		{ return wrapS; }
	public int getWrapT()		{ return wrapT; }
	public Bitmap getBitmap()	{ return bmp; }
	
	public void setTexture(int texturePtr)	{ this.texturePtr = texturePtr; }
	public void setBitmap(Bitmap bmp)		{ this.bmp = bmp; }
	public void setMinFilter(int glCap)		{ this.minFilter = glCap; }
	public void setMagFilter(int glCap)		{ this.magFilter = glCap; }
	public void setWrapS(int glCap)			{ this.wrapS = glCap; }
	public void setWrapT(int glCap)			{ this.wrapT = glCap; }
}
