package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class SpikeBall extends Entity
{	
	public SpikeBall(float size, Vector2 position)
	{
		super(new Circle(size, position, true));
	}

	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
	}
	
	public void interact(Entity ent)
	{
	    if (ent instanceof Player)
	        EntityManager.removeEntity(ent);
	}
}
