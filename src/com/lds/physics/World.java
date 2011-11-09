package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;

public class World 
{
	private Vector2 size; //assume centered at 0, 0
	private ArrayList<Shape> shapes;
	private CollisionDetector collisionDetector;
	
	public World(Vector2 size, ArrayList<Shape> shapeList)
	{
		this.size = size;	
		this.shapes = shapeList;
		collisionDetector = new CollisionDetector(new Vector2(0, 0), this.size, shapeList);
	}
	
	public void update()
	{
	    collisionDetector.update();
	    //PhysicsManager.updatePairs(collidingPairs);
	    PhysicsManager.updateShapes(shapes);
	}

	public void addShape(Shape shape)
	{ 
	    shapes.add(shape);
	}
	
	public void addShapes(ArrayList<Shape> shapes)
	{
	    shapes.addAll(shapes);
	}
	
	public void addShapes(Shape[] shapes)
	{
	    for (Shape shape : shapes)
	        this.shapes.add(shape);
	}
	
	public void removeShape(Shape shape)
	{
	    shapes.remove(shape);
	}
	
	public void removeShapes(ArrayList<Shape> shapes)
	{
	    this.shapes.removeAll(shapes);
	}
	
	public void removeShapes(Shape[] shapes)
	{
	    for (Shape shape : shapes)
	        this.shapes.remove(shape);
	}
	
	public void clearShapes()
	{
	    shapes.clear();
	}
}
