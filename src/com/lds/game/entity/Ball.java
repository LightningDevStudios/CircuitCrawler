package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.primitives.Circle;

public class Ball extends HoldObject
{
    /**
     * Initializes a new instance of the Ball class.
     * @param size The size of the ball.
     * @param position The location of the ball.
     */
	public Ball(float size, Vector2 position)
	{
		super(new Circle(size, position, true));
		this.tilesetX = 2;
		this.tilesetY = 0;
	}
}
