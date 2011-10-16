package com.lds.physics;

import com.lds.physics.Shape;
import com.lds.math.*;

import java.util.ArrayList;

public class World 
{
	private Vector2 size;
	private ArrayList<Shape> shapeList;
	private CollisionDetector collisionDetector;
	private PhysicsManager physManager;
	
	public World(Vector2 size, ArrayList<Shape> shapeList)
	{
		this.size = size;	
		this.shapeList = shapeList;
		collisionDetector = new CollisionDetector(this.size, shapeList);
		physManager = new PhysicsManager(collisionDetector);
	}

	public void addShape(Shape shape)
	{ 
	    shapeList.add(shape);
	}
	
	public void addShapes(ArrayList<Shape> shapes)
	{
	    shapeList.addAll(shapes);
	}
	
	public void addShapes(Shape[] shapes)
	{
	    for (Shape shape : shapes)
	        shapeList.add(shape);
	}
	
	public void removeShape(Shape shape)
	{
	    shapeList.remove(shape);
	}
	
	public void removeShapes(ArrayList<Shape> shapes)
	{
	    shapeList.removeAll(shapes);
	}
	
	public void removeShapes(Shape[] shapes)
	{
	    for (Shape shape : shapes)
	        shapeList.remove(shape);
	}
	
	public void clearShapes()
	{
	    shapeList.clear();
	}
}
