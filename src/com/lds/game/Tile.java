package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Tile extends Entity
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	public int tileX, tileY, tileID, texturePtr;
	public float[] texture;
	public FloatBuffer textureBuffer;
	
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
 		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtr);
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void setTexture (int ptr)
	{
		texturePtr = ptr;
	}
}
