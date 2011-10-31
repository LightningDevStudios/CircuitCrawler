package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Spike extends Entity
{
	public Spike(Vector2 position, float angle)
	{
	    this(DEFAULT_SIZE, position, angle);
	}
	
	public Spike(float size, Vector2 position, float angle)
    {
        super(new Rectangle(size, position, angle, true));
        
        this.tilesetX = 3;
        this.tilesetY = 1;
    }
}
