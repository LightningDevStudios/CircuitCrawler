package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class GridBox 
{
	public Vector4 pos;
	
	public int entityCount = 0;
	public ArrayList<Entity> detectedEnts = new ArrayList<Entity>();
	
	public GridBox(Vector4 pos)
	{
		this.pos = pos;
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
