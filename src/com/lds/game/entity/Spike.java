package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.math.Vector2;
import com.lds.physics.primitives.Rectangle;

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
	    if(ent instanceof Player)
	        EntityManager.removeEntity(ent);
	}
}
