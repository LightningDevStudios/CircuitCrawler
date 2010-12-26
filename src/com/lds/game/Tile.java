package com.lds.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Tile extends Entity
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	public int tileX, tileY, tileID;
	public float[] texture;
	public FloatBuffer textureBuffer;
	public static int[] texturePtrs = new int[1];
	public Tile(int x, int y, float _size, float _xPos, float _yPos)
	{
		super(_size, _xPos, _yPos, 0.0f);
		tileX = x;
		tileY = y;
		tileID = (y * 8) + x;
		this.texture = com.lds.TilesetHelper.getTextureVertices(x, y, 0, 7);
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtrs[0]);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void loadTexture(GL10 gl, Context context)
	{
		InputStream input = context.getResources().openRawResource(R.drawable.tilesetwire);
		Bitmap bmp = null;
		try
		{
			bmp = BitmapFactory.decodeStream(input);
		}
		finally
		{
			try
			{
				input.close();
				input = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		gl.glGenTextures(1, texturePtrs, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtrs[0]);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		
		bmp.recycle();
	}
}
