 package com.lds.game.entity;

import com.lds.Animation;

public class Sprite extends PhysEnt
{
	public Animation anim;
	
	public Sprite(float size, float xPos, float yPos)
	{
		this(size, xPos, yPos, 0.0f, 0.0f, 0.0f, null);
	}

	public Sprite(float size, float xPos, float yPos, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, moveSpeed, rotSpeed, sclSpeed, anim);
	}
	
	public Sprite(float size, float xPos, float yPos, float angle, float xScl, float yScl, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		super(size, xPos, yPos, angle, xScl, yScl, false, false, true, moveSpeed, rotSpeed, sclSpeed);

		this.anim = anim;
		texture = anim.getCurrentFrame();

		this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	@Override
	public void update()
	{
		super.update();
		anim.update();
		renderNextFrame();
	}
	
	@Override
	public void renderNextFrame()
	{
		this.texture = anim.getCurrentFrame();
		this.textureBuffer = setBuffer(textureBuffer, texture);
	}
}
