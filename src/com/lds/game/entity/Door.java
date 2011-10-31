package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Door extends Entity
{
	public Door(Vector2 position)
	{
	    this(72, position);
	}
	
	public Door(float size, Vector2 position)
    {
        super(new Rectangle(size, position, 0, new Vector2(2, 1), true));
        enableColorMode(1.0f, 1.0f, 1.0f, 1.0f);
        colorInterpSpeed = 1.0f;
        
        this.tilesetX = 2;
        this.tilesetY = 1;
    }

	public void open()
	{
		initColorInterp(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void close()
	{	
		initColorInterp(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
