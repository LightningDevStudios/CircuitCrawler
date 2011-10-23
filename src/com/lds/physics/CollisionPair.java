package com.lds.physics;

//import com.lds.math.Vector2;

public class CollisionPair 
{
	private Shape shape1;
	private Shape shape2;
	//private Vector2 nearestExit1;
	//private Vector2 nearestExit2;
	
	public CollisionPair(Shape shape1, Shape shape2)
	{
		this.shape1 = shape1;
		this.shape2 = shape2;
		//this.nearestExit1 = nearestExit1;
		//nearestExit2 = Vector2.negate(nearestExit1);
	}
	
	public Shape getEnt1()
	{
	    return shape1;
	}
	
	public Shape getEnt2()
	{
	    return shape2;
	}
	//public Vector2 getNearestExit1() { return nearestExit1; }
	//public Vector2 getNearestExit2() { return nearestExit2; }
}
