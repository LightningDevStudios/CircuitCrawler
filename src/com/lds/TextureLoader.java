package com.lds;

import javax.microedition.khronos.opengles.*;

import android.opengl.GLUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TextureLoader 
{
	
	private int[] textureFiles;
	private int texture; //the current index of textureFiles.
	
	private GL10 gl;
	private Context context;
	
	public TextureLoader(GL10 _gl, Context _context)
	{
		gl = _gl;
		context = _context;
	}
	
	public void load(int id)
	{
		//Adjust the size of the texture pointer array by handing the values to
		//a temporary array, declaring a new one, and placing the data back, but
		//leaving the last value empty
		if (textureFiles == null)
		{
			textureFiles = new int[1];
		}
		else
		{
			int[] tempTextureFiles = textureFiles;
			textureFiles = new int[textureFiles.length + 1];
			for (int i = 0; i < tempTextureFiles.length; i++)
			{
				textureFiles[i] = tempTextureFiles[i];
			}
			tempTextureFiles = null;
		}
		
		//Load a bitmap from the passed in texture (R.drawable.whatever)
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		
		//Generate a unique integer that OpenGL stores as a pointer to the texture
		gl.glGenTextures(1, textureFiles, textureFiles.length - 1);
		
		//Bind said pointer to the default texture pointer, so it render that texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureFiles[textureFiles.length - 1]);
		
		//Parameters for this texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		
		//generate the texture from the bitmap. Calling GL10.GL_TEXTURE_2D routes
		//the data back to the location stored by our textureFiles pointer
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
	}
	
	//sets the textureFiles index. This does not set the actual values!
	public void setTexture(int id)
	{
			texture = id;
	}
	
	//returns the texture pointer value, so it can be fed into
	public int getTexture()
	{
		return textureFiles[texture];
	}
}
