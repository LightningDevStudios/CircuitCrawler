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
