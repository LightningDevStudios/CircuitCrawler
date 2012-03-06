package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.primitives.Circle;
import com.lds.physics.primitives.Rectangle;

public class Ball extends HoldObject
{	
	public Ball(float size, Vector2 position)
	{
		super(new Rectangle(new Vector2(size, size), position, true));
		this.tilesetX = 2;
		this.tilesetY = 0;
	}
}
