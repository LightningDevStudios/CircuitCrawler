package com.lds.game.entity;

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
	public void update()
	{
		super.update();
	}
	
}
