package com.lds.game.entity;

import com.lds.EntityManager;
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
	
	public void interact(Entity ent)
	{
	    if (ent instanceof Player)
	        EntityManager.removeEntity(ent);
	}
}
