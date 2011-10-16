package com.lds.physics;

import java.util.ArrayList;

import com.lds.math.*;
import com.lds.game.entity.Entity;

public class CollisionDetector 
{	
	private Vector2 size;
	private ArrayList<Entity> entList;
	private Vector2 MinLeafSize = new Vector2(10,10);
	
	public CollisionDetector(Vector2 size, ArrayList<Entity> entList) 
	{
		this.size = size;
		this.entList = entList;
	}
	
	public ArrayList<ArrayList<Entity>> QuadTreeDetection()
	{
		QuadTree qt = new QuadTree(size, new Vector2(0,0), null, MinLeafSize, entList);	
		return qt.collidingEntities;
	}
	
	public boolean RadiusCheck(Entity a, Entity b)
	{
		double diagonalA = Math.sqrt((Math.pow(a.getHalfSize() * a.getXScl(), 2) + Math.pow(a.getHalfSize() * a.getYScl(), 2)));
		double diagonalB = Math.sqrt((Math.pow(b.getHalfSize() * b.getXScl(), 2) + Math.pow(b.getHalfSize() * b.getYScl(), 2)));
		return (Vector2.subtract(a.getPos(), b.getPos()).magnitude() < (float)(diagonalA + diagonalB));
	}
	
	public CollisionPair SeperatingAxisTheorem(Entity a, Entity b)
	{
		a.updateAbsolutePointLocations();
		b.updateAbsolutePointLocations();
		
		Vector2[] axes = new Vector2[4];
		axes[0] = Vector2.abs(Vector2.subtract(a.getVertVecs()[0], a.getVertVecs()[1]));
		axes[1] = Vector2.getNormal(axes[0]);
		axes[2] = Vector2.abs(Vector2.subtract(b.getVertVecs()[0], b.getVertVecs()[1]));
		axes[3] = Vector2.getNormal(axes[2]);
		
		for (Vector2 axis : axes)
		{
			axis.normalize();
						
			//get mins and maxes for first entity
			float min1 = axis.dot(a.getVertVecs()[0]);
			float max1 = min1;
			for (int i = 1; i < a.getVertVecs().length; i++)
			{
				float dotProd1 = axis.dot(a.getVertVecs()[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for second entity
			float min2 = axis.dot(b.getVertVecs()[0]);
			float max2 = min2;
			for (int i = 1; i < b.getVertVecs().length; i++)
			{
				float dotProd2 = axis.dot(b.getVertVecs()[i]);
				if (dotProd2 > max2)
					max2 = dotProd2;
				if (dotProd2 < min2)
					min2 = dotProd2;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{	
				return null;
			}
		}
		
		return new CollisionPair(a, b);
	}
}