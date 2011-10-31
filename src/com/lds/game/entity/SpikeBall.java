package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL11;

import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class SpikeBall extends Entity
{	
	public SpikeBall(float size, Vector2 position)
	{
		super(new Circle(size, position, true));
	}
	
	/**
	 * \todo anything?
	 */
	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
	}
	
}
