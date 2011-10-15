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
	private ArrayList<CollisionPair> pairList = new ArrayList<CollisionPair>();
	
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
		if(pairList.size() > 0)
			pairList.clear();
		
		QuadTreeList quadList = collisionDetector.QuadTreeDetection();
		
		for(ArrayList<Entity> ents : quadList.getOnLineEntity())
		{
			for(int i = 1; i < ents.size() - 1; i++)
			{
				if(collisionDetector.RadiusCheck(ents.get(0), ents.get(i)))
				{
					CollisionPair pair = collisionDetector.SeperatingAxisTheorem(ents.get(0), ents.get(i));
					if(pair != null)
					{
						pairList.add(pair);
					}
				}
			}
		}
		for(ArrayList<Entity> ents : quadList.getNormalEntity())
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
	}
}
