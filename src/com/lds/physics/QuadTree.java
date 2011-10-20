package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;

public class QuadTree 
{
	public ArrayList<ArrayList<Shape>> collidingEntities = new ArrayList<ArrayList<Shape>>();
	
	private ArrayList<Shape> quadTreeEntities = new ArrayList<Shape>();
	private QuadTree[] subQuads = new QuadTree[4];
	
	private QuadTree parent;
	private Vector2 size;
	private Vector2 center;
	private Vector2 minimumLeafSize;
	
	public QuadTree(Vector2 size, Vector2 center, QuadTree parent, Vector2 minimumLeafSize, ArrayList<Shape> entList)
	{
		this.parent = parent;
		this.size = size;
		this.center = center;
		this.minimumLeafSize = minimumLeafSize;
		
		SplitQuad(entList);
		
		if (parent != null)
			parent.collidingEntities.add(quadTreeEntities);
		else
			collidingEntities.add(quadTreeEntities);
	}

	public void SplitQuad(ArrayList<Shape> entList) 
	{
		if (entList.size() < 2)
			return;
		
		ArrayList<Shape> Quad1Entities = new ArrayList<Shape>();
		ArrayList<Shape> Quad2Entities = new ArrayList<Shape>();
		ArrayList<Shape> Quad3Entities = new ArrayList<Shape>();
		ArrayList<Shape> Quad4Entities = new ArrayList<Shape>();
		
		for (Shape shape : entList)
		{
			boolean inBox = true;
			for (Vector2 vert : shape.getWorldVertices())
			{
				if (!(vert.getX() > center.getX() - size.getX() / 2 && vert.getX() < center.getX() + size.getX() / 2 
				        && vert.getY() > center.getY() - size.getY() / 2 && vert.getY() < center.getY() + size.getY() / 2))
				{
					inBox = false;
				}
			}
			if (inBox)
			{
				//Quadrant1
				if (shape.getPos().getX() > center.getX() && shape.getPos().getX() < center.getX() + size.getX() / 2 
				        &&  shape.getPos().getY() > center.getY() && shape.getPos().getY() < center.getY() + size.getY() / 2)
				{
					Quad1Entities.add(shape);
				}
				//Quadrant2
				else if (shape.getPos().getX() > center.getX() - size.getX() / 2 && shape.getPos().getX() < center.getX() 
				        &&  shape.getPos().getY() > center.getY() && shape.getPos().getY() < center.getY() + size.getY() / 2)
				{
					Quad2Entities.add(shape);
				}
				//Quadrant3
				else if (shape.getPos().getX() > center.getX() - size.getX() / 2 && shape.getPos().getX() < center.getX() 
				        &&  shape.getPos().getY() > center.getY() - size.getY() / 2 && shape.getPos().getY() < center.getY())
				{
					Quad3Entities.add(shape);
				}
				//Quadrant4
				else if (shape.getPos().getX() > center.getX() && shape.getPos().getX() < center.getX() + size.getX() / 2
				        &&  shape.getPos().getY() > center.getY() - size.getY() / 2 && shape.getPos().getY() < center.getY())
				{
					Quad4Entities.add(shape);
				}
			}
			else
			{
				quadTreeEntities.add(shape);
			}
		}
		
		if (Quad1Entities.size() > 1)
		{
			if (size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad1Entities);
			else
			{
				subQuads[0] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), 
				        new Vector2(center.getX() + size.getX() / 4, center.getY() + size.getY() / 4), this, minimumLeafSize, Quad1Entities);
				Quad1Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad1Entities);
		if (Quad2Entities.size() > 1)
		{
			if (size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad2Entities);
			else
			{
				subQuads[1] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), 
				        new Vector2(center.getX() - size.getX() / 4, center.getY() + size.getY() / 4), this, minimumLeafSize, Quad2Entities);
				Quad2Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad2Entities);
		if (Quad3Entities.size() > 1)
		{
			if (size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad3Entities);
			else
			{
				subQuads[2] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), 
				        new Vector2(center.getX() - size.getX() / 4, center.getY() - size.getY() / 4), this, minimumLeafSize, Quad3Entities);
				Quad3Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad3Entities);
		if (Quad4Entities.size() > 1)
		{
			if (size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad4Entities);
			else
			{
				subQuads[3] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), 
				        new Vector2(center.getX() + size.getX() / 4, center.getY() - size.getY() / 4), this, minimumLeafSize, Quad4Entities);
				Quad4Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad4Entities);
	}
}
