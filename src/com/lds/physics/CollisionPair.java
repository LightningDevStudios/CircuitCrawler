package com.lds.physics;

import com.lds.math.Vector2;

/**
 * A class that store two colliding entities and any useful information about their collision
 * @author Devin Reed
 */
public class CollisionPair 
{
	private Shape shape1;
	private Shape shape2;
	private Vector2 nearestOut;
	
	public CollisionPair(Shape shape1, Shape shape2)
    {
        this(shape1, shape2, null);
    }
	
	public CollisionPair(Shape shape1, Shape shape2, Vector2 nearestOut)
	{
		this.shape1 = shape1;
		this.shape2 = shape2;
		this.nearestOut = nearestOut;
	}
	
	public Shape getShape1()
	{
	    return shape1;
	}
	
	public Shape getShape2()
	{
	    return shape2;
	}
	
	public Vector2 getNearestOut() 
	{ 
	    return nearestOut;
	}
}
