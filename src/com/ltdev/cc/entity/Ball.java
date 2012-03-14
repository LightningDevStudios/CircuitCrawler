package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

/**
 * A Ball is a basic circle that can be held and thrown.
 * @author Lightning Development Studios
 */
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
