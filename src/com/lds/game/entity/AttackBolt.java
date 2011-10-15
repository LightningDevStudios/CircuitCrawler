package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.math.Matrix4;
import com.lds.math.Vector2;

import java.util.ArrayList;

public class AttackBolt extends PhysEnt
{
	private Vector2 directionVec;
	private ArrayList<Entity> ignoreList;
	
	public AttackBolt(Vector2 posVec, Vector2 directionVec, float angle)
	{
		super(20.0f, posVec.getX(), posVec.getY(), false, false, 250.0f, 100.0f, 0.0f, 0.0f);
		this.directionVec = directionVec;
		this.angle = angle + 90.0f;
		rotMat = Matrix4.rotateZ((float)Math.toRadians(angle));
		//this.move(directionVec.getX() * 5.0f, directionVec.getY() * 5.0f);
		this.push(directionVec.scale(0.5f));
		this.setColorInterpSpeed(1.4f);
		this.initColorInterp(1.0f, 1.0f, 1.0f, 0.0f);
		ignoreList = new ArrayList<Entity>();
		rebuildModelMatrix();
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (!ignoreList.contains(ent))
		{
			ent.colList.remove(this);
			if (this.doesCollide(ent))
				EntityManager.removeEntity(this);
		}
	}
	
	@Override
	public void onTileInteract(Tile tile)
	{
		
	}
	
	@Override
	public void tileInteract(Tile tile)
	{
		if (tile.isWall())
		{
			colList.remove(this);
			EntityManager.removeEntity(this);
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		if (colorVec.getW() == 0.0f) //if transparent
		{
			EntityManager.removeEntity(this);
		}
	}
	
	public void ignore(Entity ent)
	{
		ignoreList.add(ent);
	}
	
	public boolean doesIgnore(Entity ent)
	{
		return ignoreList.contains(ent);
	}
}
