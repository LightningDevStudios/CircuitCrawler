package com.lds.physics;

//import com.lds.math.Vector2;

import com.lds.game.entity.Entity;

public class CollisionPair 
{
	private Entity ent1;
	private Entity ent2;
	//private Vector2 nearestExit1;
	//private Vector2 nearestExit2;
	
	public CollisionPair(Entity ent1, Entity ent2)
	{
		this.ent1 = ent1;
		this.ent2 = ent2;
		//this.nearestExit1 = nearestExit1;
		//nearestExit2 = Vector2.negate(nearestExit1);
	}
	
	public Entity getEnt1()
	{
	    return ent1;
	}
	
	public Entity getEnt2()
	{
	    return ent2;
	}
	//public Vector2 getNearestExit1() { return nearestExit1; }
	//public Vector2 getNearestExit2() { return nearestExit2; }
}
