package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.lds.Enums.RenderMode;
import com.lds.TilesetHelper;


public class Tile extends Entity
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	public int tileX, tileY, tileID;
	
	public Tile(float size, int tilePosX, int tilePosY, int tilesetX, int tilesetY)
	{
		super(size, 0, 0, RenderMode.TILESET);
		TilesetHelper.setInitialTileOffset(this, tilePosY, tilePosX, tilesetX, tilesetY);
	}
	
	@Override
	public void updateTileset(int texturePtr, int x, int y, int min, int max)
	{
		if (renderMode == RenderMode.TILESET)
		{
			this.texturePtr = texturePtr;
			texture = TilesetHelper.getTextureVertices(x, y, min, max);
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
			
			tileX = x;
			tileY = y;
		}
	}
	
	@Override
	public void updateTileset(int texturePtr, int tileID)
	{
		if (renderMode == RenderMode.TILESET)
		{
			this.texturePtr = texturePtr;
			texture = TilesetHelper.getTextureVertices(tileID);
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	@Override
	public void updateTileset(int x, int y, int min, int max)
	{
		if (renderMode == RenderMode.TILESET)
		{
			texture = TilesetHelper.getTextureVertices(x, y, min, max);
		
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	@Override
	public void updateTileset(int tileID)
	{
		if (renderMode == RenderMode.TILESET)
		{
			texture = TilesetHelper.getTextureVertices(tileID);
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
}
