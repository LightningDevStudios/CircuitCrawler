package com.lds;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TextureLoader 
{
	private int[] tempTexture;
	
	private GL10 gl;
	
	public TextureLoader(GL10 gl)
	{
		this.gl = gl;
		tempTexture = new int[1];
	}
	
	public void loadTexture(Texture tex)
	{		
		//Generate a unique integer that OpenGL stores as a pointer to the texture
		gl.glGenTextures(1, tempTexture, 0);
		
		//Bind said pointer to the default texture pointer, so it render that texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tempTexture[0]);
		
		//Parameters for this texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, tex.getMinFilter());
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, tex.getMagFilter());
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, tex.getWrapS());
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, tex.getWrapT());
		
		//generate the texture from the bitmap. Calling GL10.GL_TEXTURE_2D routes
		//the data back to the location stored by our textureFiles pointer
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tex.getBitmap(), 0);
		
		tex.setTexture(tempTexture[0]);
	}
		
	public void reload(Texture tex)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexture());
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tex.getBitmap(), 0);
	}
}
