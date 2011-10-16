package com.lds.physics;

import java.util.ArrayList;
import com.lds.game.entity.*;

public class PhysicsManager 
{
	/***********
	 * Members *
	 ***********/

    /**
     * 
     */
	private CollisionDetector collisionDetector;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Initializes a new instance of the PhysicsManager class.
	 * @param collisionDetector A collision detector object.
	 */
	public PhysicsManager(CollisionDetector collisionDetector)
	{
		this.collisionDetector = collisionDetector;
	}

	/**
	 * Get entities from a Collision Detector.
	 * <ol>
	 *     <li>Run QuadTreeDetection();</li>
	 *     <li>Use the QuadTreeList from QuadTreeDetection() and pass the entities to RadiusCheck.</li>
	 *     <li>If the RadiusCheck returns true pass them to the SeperatingAxisTheorem.</li>
	 *     <li>Solve the Collisions from the CollisionPair.</li>
	 * </ol>
	 * 
	 * Note the QuadTreeDetection() returns 2 list of lists:
	 * <ol>
	 *     <li>List1 called normalEntity contains all entities that could be colliding with each other so if
	 *     there is 1 list in normalEntity and it contains 3 entities; entity 1 compares to entity 2 and 3,
	 *     and entity 2 compares to 3</li>
	 *     <li>List 2 called onLineEntity contains all entities that do not fall in a specific quadrant so
	 *     if onLineEntity contains 1 list which has 6 entities in it; entity 1 is compared to
	 *     entity 2, 3, 4, 5, and 6.</li>
	 * </ol>
	 * @return 
	 */
	public ArrayList<CollisionPair> SolveCollision()
	{
		ArrayList<CollisionPair> pairList= new ArrayList<CollisionPair>();
		
		ArrayList<ArrayList<Entity>> quadList = collisionDetector.QuadTreeDetection();
		
		for(ArrayList<Entity> ents : quadList)
		{
			for(Entity ent : ents)
			{
				for(Entity en : ents)
				{
					if((!(ent.equals(en))) && collisionDetector.RadiusCheck(ent, en))
					{
						CollisionPair pair = collisionDetector.SeperatingAxisTheorem(ent, en);
						if(pair != null)
						{
							pairList.add(pair);
						}
					}
				}
			}
		}
		return pairList;
	}
}
