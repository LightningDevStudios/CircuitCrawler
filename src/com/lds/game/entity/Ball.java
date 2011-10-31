package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class Ball extends HoldObject
{	
	public Ball(float size, Vector2 position)
	{
		super(new Circle(size, position, true));
		this.tilesetX = 2;
		this.tilesetY = 0;
	}
}
