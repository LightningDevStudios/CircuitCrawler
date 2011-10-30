package com.lds;

import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL11;

/**
 * A static helper class used to load textures.
 * \todo get rid of this, move it into Texture?
 * @author Lightning Development Studios
 */
public final class TextureLoader 
{
	private TextureLoader()
	{
	}
	
	public static void loadTexture(GL11 gl, Texture tex)
	{
		//Generate a unique integer that OpenGL stores as a pointer to the texture
	    int[] tempTexture = new int[1];
		gl.glGenTextures(1, tempTexture, 0);
		
		//Bind said pointer to the default texture pointer, so it render that texture
		gl.glBindTexture(GL11.GL_TEXTURE_2D, tempTexture[0]);
		
		//Parameters for this texture
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, tex.getMinFilter());
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, tex.getMagFilter());
		
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, tex.getWrapS());
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, tex.getWrapT());
		
		//generate the texture from the bitmap. Calling GL10.GL_TEXTURE_2D routes
		//the data back to the location stored by our textureFiles pointer
		GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, tex.getBitmap(), 0);
		
		tex.setTexture(tempTexture[0]);
	}
		
	public static void reload(GL11 gl, Texture tex)
	{
		gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
		
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, tex.getBitmap(), 0);
	}
}
