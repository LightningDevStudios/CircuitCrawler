package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

public class Spike extends Entity
{
	public Spike(Vector2 position, float angle)
	{
	    this(DEFAULT_SIZE, position, angle);
	}
	
	public Spike(float size, Vector2 position, float angle)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        
        this.tilesetX = 3;
        this.tilesetY = 1;
    }
	
	@Override
	public void interact(Entity ent)
	{
	    if (ent instanceof Player)
	        EntityManager.removeEntity(ent);
	}
}
