package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.lds.Enums.RenderMode;

public class Sprite extends PhysEnt
{
	public int maxX, maxY, curX, curY, curFrame;
	
	public Sprite(float size, float xPos, float yPos, float moveSpeed, float rotSpeed, float sclSpeed, int maxX, int maxY)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, moveSpeed, rotSpeed, sclSpeed, maxX, maxY);
	}
	
	public Sprite(float size, float xPos, float yPos, float angle, float xScl, float yScl, float moveSpeed, float rotSpeed, float sclSpeed, int maxX, int maxY)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, RenderMode.TILESET, moveSpeed, rotSpeed, sclSpeed);
		this.maxX = maxX;
		this.maxY = maxY;
		
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
	public void update()
	{
		renderNextFrame();
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
		this.texture = com.lds.TilesetHelper.getTextureVertices(curX, curY, 0, maxX);
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	public void setTexture(int ptr)
	{
		texturePtr = ptr;
	}
}
