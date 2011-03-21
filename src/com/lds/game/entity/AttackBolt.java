package com.lds.game.entity;

import java.util.ArrayList;

import com.lds.Vector2f;
import com.lds.EntityManager;

public class AttackBolt extends PhysEnt
{
	Vector2f directionVec;
	ArrayList<Entity> ignoreList;
	
	public AttackBolt(Vector2f posVec, Vector2f directionVec, float angle)
	{
		super(20.0f, posVec.getX(), posVec.getY(), false, false, 250.0f, 100.0f, 0.0f, 0.0f);
		this.directionVec = directionVec;
		this.angle = angle;
		//this.move(directionVec.getX() * 5.0f, directionVec.getY() * 5.0f);
		this.push(directionVec.scale(0.5f));
		this.enableColorMode(1.0f, 0.0f, 0.0f, 1.0f);
		this.setColorInterpSpeed(1.4f);
		this.initColorInterp(0.0f, 0.0f, 0.0f, 0.0f);
		ignoreList = new ArrayList<Entity>();
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
		if (this.getColorA() == 0.0f)
		{
			EntityManager.removeEntity(this);
		}
	}
	
	public void ignore (Entity ent)
	{
		ignoreList.add(ent);
	}
	
	public boolean doesIgnore (Entity ent)
	{
		return ignoreList.contains(ent);
	}
}
