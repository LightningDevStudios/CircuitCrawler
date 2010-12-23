package com.lds;

import javax.microedition.khronos.opengles.*;

import android.opengl.GLUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.Integer;
import java.util.HashMap;

public class TextureLoader 
{
	private HashMap<Integer, Integer> textureMap;
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
	
	public TextureLoader(GL10 _gl, Context _context)
	{
		gl = _gl;
		context = _context;
		textureMap = new HashMap<Integer,Integer>();
	}
	
	public void load()
	{
		int[] temp_texture = new int[textureFiles.length];
		gl.glGenTextures(textureFiles.length, temp_texture, 0);
		textures = temp_texture;
		for (int i = 0; i < textureFiles.length; i++)
		{
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), textureFiles[i]);
			this.textureMap.put(new Integer(textureFiles[i]), new Integer(i));
			int tex = temp_texture[i];
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
			
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		}
	}
	
	public void addTexture(int resource)
	{
		if (textureFiles == null)
		{
			textureFiles = new int[1];
			textureFiles[0] = resource;
		}
		else
		{
			int[] temp_array = new int[textureFiles.length + 1];
			for (int i = 0; i < temp_array.length; i++)
			{
				temp_array[i] = textureFiles[i];
			}
			temp_array[textureFiles.length] = resource;
			textureFiles = temp_array;
		}
	}
	
	public void setTexture(int id)
	{
		try
		{
			int textureID = this.textureMap.get(new Integer(id)).intValue();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureID]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
