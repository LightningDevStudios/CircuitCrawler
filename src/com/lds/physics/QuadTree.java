package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

public class QuadTree 
{
	public ArrayList<ArrayList<Entity>> collidingEntities = new ArrayList<ArrayList<Entity>>();
	
	private ArrayList<Entity> quadTreeEntities = new ArrayList<Entity>();
	private QuadTree[] subQuads = new QuadTree[4];
	
	private QuadTree parent;
	private Vector2 size;
	private Vector2 center;
	private Vector2 minimumLeafSize;
	
	public QuadTree(Vector2 size, Vector2 center, QuadTree parent, Vector2 minimumLeafSize, ArrayList<Entity> entList)
	{
		this.parent = parent;
		this.size = size;
		this.center = center;
		this.minimumLeafSize = minimumLeafSize;
		
		SplitQuad(entList);
		
		if(parent != null)
			parent.collidingEntities.add(quadTreeEntities);
		else
			collidingEntities.add(quadTreeEntities);
	}

	public void SplitQuad(ArrayList<Entity> entList) 
	{
		if(entList.size() < 2)
			return;
		
		ArrayList<Entity> Quad1Entities = new ArrayList<Entity>();
		ArrayList<Entity> Quad2Entities = new ArrayList<Entity>();
		ArrayList<Entity> Quad3Entities = new ArrayList<Entity>();
		ArrayList<Entity> Quad4Entities = new ArrayList<Entity>();
		
		for(Entity ent : entList)
		{
			boolean inBox = true;
			for(Vector2 vert : ent.getVertVecs()) //What if it is circle are there 360 verts?
			{
				if(!(vert.getX() > center.getX() - size.getX() / 2 && vert.getX() < center.getX() + size.getX() / 2 && vert.getY() > center.getY() - size.getY() / 2 && vert.getY() < center.getY() + size.getY() / 2))
				{
					inBox = false;
				}
			}
			if(inBox)
			{
				//Quadrant1
				if(ent.getPos().getX() > center.getX() && ent.getPos().getX() < center.getX() + size.getX() / 2 &&  ent.getPos().getY() > center.getY() && ent.getPos().getY() < center.getY() + size.getY() / 2)
				{
					Quad1Entities.add(ent);
				}
				//Quadrant2
				else if(ent.getPos().getX() > center.getX() - size.getX() / 2 && ent.getPos().getX() < center.getX() &&  ent.getPos().getY() > center.getY() && ent.getPos().getY() < center.getY() + size.getY() / 2)
				{
					Quad2Entities.add(ent);
				}
				//Quadrant3
				else if(ent.getPos().getX() > center.getX() - size.getX() / 2 && ent.getPos().getX() < center.getX() &&  ent.getPos().getY() > center.getY() - size.getY() / 2 && ent.getPos().getY() < center.getY())
				{
					Quad3Entities.add(ent);
				}
				//Quadrant4
				else if(ent.getPos().getX() > center.getX() && ent.getPos().getX() < center.getX() + size.getX() / 2 &&  ent.getPos().getY() > center.getY() - size.getY() / 2 && ent.getPos().getY() < center.getY())
				{
					Quad4Entities.add(ent);
				}
			}
			else
			{
				quadTreeEntities.add(ent);
			}
		}
		
		if(Quad1Entities.size() > 1)
		{
			if(size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad1Entities);
			else
			{
				subQuads[0] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), new Vector2(center.getX() + size.getX() / 4, center.getY() + size.getY() / 4), this, minimumLeafSize, Quad1Entities);
				Quad1Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad1Entities);
		if(Quad2Entities.size() > 1)
		{
			if(size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad2Entities);
			else
			{
				subQuads[1] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), new Vector2(center.getX() - size.getX() / 4, center.getY() + size.getY() / 4), this, minimumLeafSize, Quad2Entities);
				Quad2Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad1Entities);
		if(Quad3Entities.size() > 1)
		{
			if(size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad3Entities);
			else
			{
				subQuads[2] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), new Vector2(center.getX() - size.getX() / 4, center.getY() - size.getY() / 4), this, minimumLeafSize, Quad3Entities);
				Quad3Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad1Entities);
		if(Quad4Entities.size() > 1)
		{
			if(size.getX() <= minimumLeafSize.getX() && size.getY() <= minimumLeafSize.getY())
				quadTreeEntities.addAll(Quad4Entities);
			else
			{
				subQuads[3] = new QuadTree(new Vector2(size.getX() / 2, size.getY() / 2), new Vector2(center.getX() + size.getX() / 4, center.getY() - size.getY() / 4), this, minimumLeafSize, Quad4Entities);
				Quad4Entities.clear();
			}
		}
		else
			quadTreeEntities.addAll(Quad1Entities);
	}
}
