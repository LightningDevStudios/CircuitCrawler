package com.lds.physics;

import com.lds.game.entity.Entity;
import com.lds.math.*;

import java.util.ArrayList;

public class CollisionDetector 
{	
	private Vector2 size;
	private ArrayList<Shape> shapeList;
	
	public CollisionDetector(Vector2 size, ArrayList<Shape> shapeList) 
	{
		this.size = size;
		this.shapeList = shapeList;
	}
	
	public QuadTreeList QuadTreeDetection()
	{
	    //TODO: shapes
		Grid masterGrid = new Grid(size, new Vector2(0, 0), 0, entList, null);	
		masterGrid.SearchGrid(masterGrid);
		return new QuadTreeList(masterGrid.getColEnts(), masterGrid.getColEntsOnLines());
	}
	
	public static boolean RadiusCheck(Shape a, Shape b)
    {
        float diagonalA = Vector2.subtract(a.getPos(), a.getWorldVertices()[0]).length();
        float diagonalB = Vector2.subtract(b.getPos(), b.getWorldVertices()[0]).length();
        return Vector2.subtract(a.getPos(), b.getPos()).length() < (float)(diagonalA + diagonalB);
    }
	
	public static boolean RadiusCheckCircles(Circle a, Circle b)
	{
	    float distance = Vector2.subtract(a.getPos(), b.getPos()).length();
	    float colDistance = a.getVertices()[5] * a.getScale().getX() - b.getVertices()[5] * b.getScale().getY();
	    return distance < colDistance;
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
				float dotProd1 = axis.dot(a.getWorldVertices()[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for second entity
			float min2 = axis.dot(b.getWorldVertices()[0]);
			float max2 = min2;
			for (int i = 1; i < b.getWorldVertices().length; i++)
			{
				float dotProd2 = axis.dot(b.getWorldVertices()[i]);
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
}
