package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;
 
/**
 * \todo: BETTER NAMES FOR ALL METHODS
 * \todo: IMPLEMENT INTERACTION
 */
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
		QuadTree qt = new QuadTree(size, new Vector2(0, 0), null, new Vector2(50, 50), shapeList);
		return qt.collidingEntities;
	}
	
	/**
     * Get colliding shapes
     * <ol>
     *     <li>Run QuadTreeDetection();</li>
     *     <li>Use the QuadTreeList from QuadTreeDetection() and pass the shapes to RadiusCheck.</li>
     *     <li>If the RadiusCheck returns true pass them to the SeperatingAxisTheorem.</li>
     *     <li>Solve the Collisions from the CollisionPair.</li>
     * </ol>
     * 
     * Note the QuadTreeDetection() returns 2 list of lists:
     * <ol>
     *     <li>List 1 called normalShape contains all shapes that could be colliding with each other so if
     *     there is 1 list in normalShape and it contains 3 shapes; shape 1 compares to shape 2 and 3,
     *     and shape 2 compares to 3</li>
     *     <li>List 2 called onLineShape contains all shapes that do not fall in a specific quadrant so
     *     if onLineShape contains 1 list which has 6 shapes in it; shape 1 is compared to
     *     shape 2, 3, 4, 5, and 6.</li>
     * </ol>
     * @return 
     */
    public ArrayList<CollisionPair> SolveCollision()
    {
        ArrayList<CollisionPair> pairs = new ArrayList<CollisionPair>();
        ArrayList<ArrayList<Shape>> shapeListList = QuadTreeDetection();
        
        //TODO: moer effiecency
        for (ArrayList<Shape> shapeList : shapeListList)
        {
            for (Shape shape : shapeList)
            {
                for (Shape s : shapeList)
                {
                    if (s != shape)
                    {
                        CollisionPair pair = medAndNearPhase(shape, s);
                        if (pair != null)
                            pairs.add(pair);
                    }
                }
            }
        }
        
        return pairs;
    }
    
    /******************
     * Static Methods *
     ******************/
	
	public static boolean radiusCheck(Shape a, Shape b)
    {
        float diagonalA = Vector2.subtract(a.getPos(), a.getWorldVertices()[0]).length();
        float diagonalB = Vector2.subtract(b.getPos(), b.getWorldVertices()[0]).length();
        return Vector2.subtract(a.getPos(), b.getPos()).length() < (float)(diagonalA + diagonalB);
    }
	
	public static CollisionPair medAndNearPhase(Shape a, Shape b)
	{
	    if (a instanceof Circle && b instanceof Circle)
    	    return radiusCheckCircles((Circle)a, (Circle)b);
	    else if (!radiusCheck(a, b))
	            return null;
	        
	    if (a instanceof Rectangle)
	    {
	        if (b instanceof Circle)
	            return satRectangleCircle((Rectangle)a, (Circle)b);
	        else if (b instanceof Rectangle)
	            return satRectangles((Rectangle)a, (Rectangle)b);
	    }
	    else if (b instanceof Circle && b instanceof Rectangle)
	    {
	        return satRectangleCircle((Rectangle)b, (Circle)a);
	    }
	    
	    return null;
	}
	
	public static CollisionPair radiusCheckCircles(Circle a, Circle b)
	{
	    float distance = Vector2.subtract(a.getPos(), b.getPos()).length();
	    return distance < a.getRadius() - b.getRadius() ? new CollisionPair(a, b) : null;
	}
	
	public static CollisionPair satRectangleCircle(Rectangle r, Circle c)
	{      
       Vector2[] axes = new Vector2[3];
       //set rectangle-based axes
       axes[0] = Vector2.abs(Vector2.subtract(r.getVectorVertices()[0], r.getVectorVertices()[1]));
       axes[1] = Vector2.getNormal(axes[0]);
       //set circle axis
       axes[2] = Vector2.subtract(r.getVectorVertices()[0], c.getPos());
       for (int i = 1; i < r.getVectorVertices().length; i++)
       {
           Vector2 tempVec = Vector2.subtract(r.getVectorVertices()[i], c.getPos());
           if (axes[2].length() > tempVec.length())
           {
               axes[2] = tempVec;
           }
       }
       
       for (Vector2 axis : axes)
       {
           axis = Vector2.normalize(axis);
                       
           //get mins and maxes for rectangle
           float min1 = Vector2.dot(axis, r.getVectorVertices()[0]);
           float max1 = min1;
           for (int i = 1; i < r.getVectorVertices().length; i++)
           {
               float dotProd1 = Vector2.dot(axis, r.getVectorVertices()[i]);
               if (dotProd1 > max1)
                   max1 = dotProd1;
               if (dotProd1 < min1)
                   min1 = dotProd1;
           }
           
           //get mins and maxes for circle
           //TODO: find a better way to do this
           float min2 = Vector2.dot(axis, Vector2.add(c.getPos(), Vector2.scale(axis, c.getRadius())));
           float max2 = Vector2.dot(axis, Vector2.subtract(c.getPos(), Vector2.scale(axis, c.getRadius())));
           if (min2 > max2)
           {
               float temp = min2;
               min2 = max2;
               max2 = temp;
           }
           
           if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
               return null;
       }
       
       return new CollisionPair(r, c);
	}
	
	public static CollisionPair satRectangles(Rectangle a, Rectangle b)
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
				return null;
		}
		
		return new CollisionPair(a, b);
	}
	
	public static boolean containsPoint(Circle c, Vector2 v)
	{
	    return Vector2.subtract(c.getPos(), v).length() < c.getRadius();
	}
	
	public static boolean containsPoint(Rectangle r, Vector2 v)
    {
        Vector2[] verts = r.getWorldVertices();
        Vector2 axis = Vector2.subtract(verts[0], verts[1]);     
        
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
