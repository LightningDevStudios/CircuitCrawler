package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums.RenderMode;


public class Tile extends Entity
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	public int tileX, tileY, tileID;
	
	public Tile(int x, int y, float _size, float _xPos, float _yPos)
	{
		super(_size, _xPos, _yPos, RenderMode.TILESET);
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
		
	public void setTexture (int ptr)
	{
		texturePtr = ptr;
	}
}
