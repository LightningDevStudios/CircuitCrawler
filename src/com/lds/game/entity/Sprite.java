package com.lds.game.entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.lds.Animation;

public class Sprite extends PhysEnt
{
	public Animation anim;
	
	//TODO pass in a texture, set renderMode manually
	public Sprite(float size, float xPos, float yPos, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, moveSpeed, rotSpeed, sclSpeed, anim);
	}
	
	public Sprite(float size, float xPos, float yPos, float angle, float xScl, float yScl, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, false, moveSpeed, rotSpeed, sclSpeed);

		this.anim = anim;
		texture = anim.getCurrentFrame();

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	@Override
	public void update()
	{
		super.update();
		anim.update();
		
	}
	
	@Override
	public void renderNextFrame()
	{
		this.texture = anim.getCurrentFrame();
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
}
