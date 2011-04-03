package com.lds.game.entity;

public abstract class StaticEnt extends Entity //static obejcts are immovable, such as interactive switches and devices and immovable blocks
{
	public StaticEnt(float size, float xPos, float yPos, boolean circular, boolean willCollide)
	{
		super(size, xPos, yPos, circular, willCollide);
	}
	
	public StaticEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
	}
	
	@Override
	public boolean isColliding(Entity ent)
	{
		if (ent instanceof Tile || ent instanceof StaticEnt)
			return false;
		
		return  super.isColliding(ent);
	}
}
