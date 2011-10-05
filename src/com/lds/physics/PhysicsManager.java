package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

public class PhysicsManager 
{
	/***********
	 * Members *
	 ***********/

	private CollisionDetector collisionDetector;
	
	/****************
	 * Constructors 
	 * @param collisionDetector *
	 ****************/
	
	public PhysicsManager(CollisionDetector collisionDetector)
	{
		this.collisionDetector = collisionDetector;
	}
	
	//Get entities from a Collision Detector
		//Step 1: Run QuadTreeDetection();
		//Step 2: Use the QuadTreeList from QuadTreeDetection() and pass the entities to RadiusCheck.
		//Step 3: If the RadiusCheck returns true pass them to the SeperatingAxisTheorem
		//Step 4: Solve the Collisions from the CollisionPair
	
	//Note the QuadTreeDetection() returns 2 list of lists.
		//List1 called normalEntity contains all entities that could be colliding with each other so if 
		//there is 1 list in normalEntity and it contains 3 entities; entity 1 compares to entity 2 and 3, and entity 2 compares to 3
	
		//List 2 called onLineEntity contains all entities that do not fall in a specific quadrant so if onLineEntity contains 1 list 
		//which has 6 entities in it; entity 1 is compared to entity 2, 3, 4, 5, and 6.
	public void SolveCollision()
	{
		//TODO Put all physics calculations in here 
	}
}
