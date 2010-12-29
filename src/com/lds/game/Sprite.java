package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Sprite extends PhysEnt
{
	public int maxX, maxY, curX, curY, curFrame;
	//public float[] texture;
	//public FloatBuffer textureBuffer;
	//public int texturePtr;
	
	public Sprite(int _maxX, int _maxY, float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl, float _speed)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, PhysEnt.DEFAULT_SPEED);
		maxX = _maxX;
		maxY = _maxY;
		
		curX = 0;
		curY = 0;
		texture = com.lds.TilesetHelper.getTextureVertices(0, 0, 0, maxY);

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	@Override
	public void renderNextFrame()
	{
		if (curX == maxX)
		{
			curX = 0;
			
			if (curY == maxY)
			{
				curY = 0;
			}
			else
			{
				curY++;
			}
		}
		else
		{
			curX++;
		}
		curFrame = (curY + 1) * maxX + curX;
		//System.out.println(curFrame);
		this.texture = com.lds.TilesetHelper.getTextureVertices(curX, curY, 0, maxX);
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	@Override
	/*public void draw(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtr);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}*/
	
	public void setTexture(int ptr)
	{
		texturePtr = ptr;
	}
}
