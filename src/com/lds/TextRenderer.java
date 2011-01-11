package com.lds;

import com.lds.game.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TextRenderer 
{
	Context context;
	Bitmap textTileset;
	
	public TextRenderer(Context context)
	{
		this.context = context;
		textTileset = BitmapFactory.decodeResource(context.getResources(), R.drawable.text);
	}
	
	public Bitmap textToBitmap(String text, int xSize, int ySize)
	{
		//Create a new, blank bitmap. Also allocate an int array which the Bitmap class uses to store in getPixels() and read in setPixels()
		//TODO check for ySize with the number of \n chars
		Bitmap bmp = Bitmap.createBitmap(text.length() * xSize, ySize, Bitmap.Config.ARGB_8888);
		int[] charPixels = new int[xSize * ySize * 4];
		
		//Go through each character, grab the proper pixels from R.drawable.text, by using the native ASCII conversion, String.charAt().
		//Then write those referenced pixels back to the text bitmap
		for (int i = 0; i < text.length(); i++)
		{
			textTileset.getPixels(charPixels, 0, xSize, TilesetHelper.getTilesetX((int)text.charAt(i), 16) * xSize, TilesetHelper.getTilesetY((int)text.charAt(i), 16) * ySize, xSize, ySize);
			bmp.setPixels(charPixels, 0, xSize, xSize * i, 0, xSize, ySize);
		}
		
		return bmp;
	}
}
