package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;

public class CollisionDetector 
{	
	private Vector2 size;
	private ArrayList<Shape> shapeList;
	private Vector2 minLeafSize = new Vector2(10, 10);
	
	public CollisionDetector(Vector2 size, ArrayList<Shape> shapeList) 
	{
		this.size = size;
		this.shapeList = shapeList;
	}

	public ArrayList<ArrayList<Shape>> QuadTreeDetection()
	{
		QuadTree qt = new QuadTree(size, new Vector2(0, 0), null, minLeafSize, shapeList); //TODO: convert to shapes
		return qt.collidingEntities;
	}
	
	public static boolean RadiusCheck(Shape a, Shape b)
    {
        float diagonalA = Vector2.subtract(a.getPos(), a.getWorldVertices()[0]).length();
        float diagonalB = Vector2.subtract(b.getPos(), b.getWorldVertices()[0]).length();
        return Vector2.subtract(a.getPos(), b.getPos()).length() < (float)(diagonalA + diagonalB);
    }
	
	/**
	 * \todo rethink collision, maybe remove CollisionPair and stop calling this from PhysicsManager.
	 * @param a
	 * @param b
	 * @return
	 */
	public static CollisionPair CheckCollision(Shape a, Shape b)
	{
	    return null;
	}
	
	public static boolean RadiusCheckCircles(Circle a, Circle b)
	{
	    float distance = Vector2.subtract(a.getPos(), b.getPos()).length();
	    return distance < a.getRadius() - b.getRadius();
	}
	
	public static boolean RadiusCheckCircleRectangle(Rectangle r, Circle c)
	{
       for (Vector2 v : r.getWorldVertices())
       {
           if (Vector2.subtract(v, c.getPos()).length() < c.getRadius())
               return true;
       }
       
       return false;
	}
	
	public static boolean SATRectangles(Rectangle a, Rectangle b)
	{	
		Vector2[] axes = new Vector2[4];
		axes[0] = Vector2.abs(Vector2.subtract(a.getWorldVertices()[0], a.getWorldVertices()[1]));
		axes[1] = Vector2.getNormal(axes[0]);
		axes[2] = Vector2.abs(Vector2.subtract(b.getWorldVertices()[0], b.getWorldVertices()[1]));
		axes[3] = Vector2.getNormal(axes[2]);
		
		for (Vector2 axis : axes)
		{
			axis = Vector2.normalize(axis);
						
			//get mins and maxes for first entity
			float min1 = Vector2.dot(axis, a.getWorldVertices()[0]);
			float max1 = min1;
			for (int i = 1; i < a.getWorldVertices().length; i++)
			{
				float dotProd1 = Vector2.dot(axis, a.getWorldVertices()[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for second entity
			float min2 = Vector2.dot(axis, b.getWorldVertices()[0]);
			float max2 = min2;
			for (int i = 1; i < b.getWorldVertices().length; i++)
			{
				float dotProd2 = Vector2.dot(axis, b.getWorldVertices()[i]);
				if (dotProd2 > max2)
					max2 = dotProd2;
				if (dotProd2 < min2)
					min2 = dotProd2;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{	
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean containsPoint(Circle c, Vector2 v)
	{
	    return Vector2.subtract(c.getPos(), v).length() < c.getRadius();
	}
	
	/**
     *  \todo get rid of abs on line 2?
     */
	public static boolean containsPoint(Rectangle r, Vector2 v)
    {
        Vector2[] verts = r.getWorldVertices();
        Vector2 axis = Vector2.abs(Vector2.subtract(verts[0], verts[1]));
        
        
        for (int i = 0; i < 2; i++)
        {
            //get mins and maxes for entity
            float min = Vector2.dot(axis, verts[0]);
            float max = min;
            for (int j = 1; j < verts.length; j++)
            {
                float dotProd = Vector2.dot(axis, verts[j]);
                if (dotProd > max)
                    max = dotProd;
                if (dotProd < min)
                    min = dotProd;
            }
            
            //gets projection of vector
            float projection = Vector2.dot(axis, v);
            
            if (projection < min || projection > max)
                return false;
            
            axis = Vector2.getNormal(axis);
        }
        
        return true;
    }
}
