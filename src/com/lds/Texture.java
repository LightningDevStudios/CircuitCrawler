package com.lds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//In C++ this would be a struct, but alas...
public class Texture 
{
	
	//TODO find a method to store GL caps for texture params
	private int id, xSize, ySize, xPixels, yPixels, xTiles, yTiles, texturePtr;
	private Bitmap bmp;
	
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Context context)
	{
		this(texID, xSize, ySize, xTiles, yTiles, BitmapFactory.decodeResource(context.getResources(), texID));
	}
	
	public Texture(int texID, int xSize, int ySize, int xTiles, int yTiles, Bitmap bmp)
	{
		this.id = texID;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xTiles = xTiles;
		this.yTiles = yTiles;
		this.xPixels = xSize / xTiles;
		this.yPixels = ySize / yTiles;
		this.bmp = bmp;
	}
	
	public int getTextureID()	{ return id; }
	public int getXSize()		{ return xSize; }
	public int getYSize()		{ return ySize; }
	public int getXPixPerTile()	{ return xPixels; }
	public int getYPixPerTile()	{ return yPixels; }
	public int getXTiles()		{ return xTiles; }
	public int getYTiles()		{ return yTiles; }
	public int getTexture()		{ return texturePtr; }
	public Bitmap getBitmap()	{ return bmp; }
	
	public void setTexture(int texturePtr)	{ this.texturePtr = texturePtr; }
}
