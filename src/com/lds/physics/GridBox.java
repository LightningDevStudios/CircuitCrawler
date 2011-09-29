package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class GridBox 
{
	public Vector4 pos;
	public Vector2 center;
	public float gridSize;
	
	public int entityCount = 0;
	public ArrayList<Entity> detectedEnts = new ArrayList<Entity>();
	
	public GridBox(Vector4 pos, Vector2 center, float gridSize)
	{
		this.pos = pos;
		this.center = center;
		this.gridSize = gridSize;
	}
	
	public boolean IsInBox(Vector2 point, Entity ent)
	{
		if(point.getX() >= pos.getX() && point.getX() <= pos.getZ() && point.getY() >= pos.getW() && point.getY() <= pos.getY())
		{
			detectedEnts.add(ent);
			return true;
		}
		return false;
	}
}
