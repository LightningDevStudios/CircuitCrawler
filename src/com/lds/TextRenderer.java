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
	
	public Bitmap textToBitmap(String text)
	{
		//Create a new, blank bitmap. Also allocate an int array which the Bitmap class uses to store in getPixels() and read in setPixels()
		Bitmap bmp = Bitmap.createBitmap(text.length() * 16, 32, Bitmap.Config.ARGB_8888);
		int[] charPixels = new int[2048];
		
		//Go through each character, grab the proper pixels from R.drawable.text, by using the native ASCII conversion, String.charAt().
		//Then write those referenced pixels back to the text bitmap
		for (int i = 0; i < text.length(); i++)
		{
			textTileset.getPixels(charPixels, 0, 16, TilesetHelper.getTilesetX((int)text.charAt(i), 16) * 16, TilesetHelper.getTilesetY((int)text.charAt(i), 16) * 32, 16, 32);
			bmp.setPixels(charPixels, 0, 16, 16 * i, 0, 16, 32);
		}
		
		return bmp;
	}
}
