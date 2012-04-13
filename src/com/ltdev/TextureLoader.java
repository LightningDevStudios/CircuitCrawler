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
