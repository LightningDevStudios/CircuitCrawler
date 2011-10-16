 package com.lds.game.entity;

import com.lds.Animation;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Sprite extends Entity
{
	public Animation anim;
	
	public Sprite(float size, Vector2 position, Animation anim)
	{
		super(new Rectangle(size, position, false));
		this.anim = anim;
		
		//texture = anim.getCurrentFrame();
        //this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	/**
	 * \todo some animation stuff.
	 */
	@Override
	public void update()
	{
		super.update();	
	}
	
	/**
	 * \todo some animation stuff.
	 */
	public void renderNextFrame()
	{
		//this.texture = anim.getCurrentFrame();
		//this.textureBuffer = setBuffer(textureBuffer, texture);
	}
}
