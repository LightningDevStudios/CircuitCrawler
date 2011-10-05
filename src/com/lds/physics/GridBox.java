package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.game.entity.PhysBall;
import com.lds.math.*;

public class GridBox 
{
	public Vector4 pos;
	public Vector2 center;
	public Vector2 gridSize;
	
	public int quadrant;
	public int entityCount = 0;
	public ArrayList<Entity> detectedEnts = new ArrayList<Entity>();
	
	public GridBox(Vector4 pos, Vector2 center, Vector2 gridSize, int quadrant)
	{
		this.quadrant = quadrant;
		this.pos = pos;
		this.center = center;
		this.gridSize = gridSize;
	}
	
	public boolean IsInBox(Entity ent)
	{
		return false; //TODO Bounding Box Check
	}
}
