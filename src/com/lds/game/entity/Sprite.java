 package com.lds.game.entity;

import com.lds.Animation;

import com.lds.game.ai.*;

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
		super(size, xPos, yPos, angle, xScl, yScl, false, false, true, moveSpeed, rotSpeed, sclSpeed, 0.0f);
		this.anim = anim;
		//texture = anim.getCurrentFrame();

		//this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	@Override
	public void update()
	{
		super.update();
		
	}
	
	@Override
	public void renderNextFrame()
	{
		//this.texture = anim.getCurrentFrame();
		//this.textureBuffer = setBuffer(textureBuffer, texture);
	}
}
