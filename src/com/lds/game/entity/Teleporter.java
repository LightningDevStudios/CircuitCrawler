package com.lds.game.entity;

import com.lds.Vector2f;

public class Teleporter extends StaticEnt
{
	protected boolean active;
	protected Object tempEnt;
	
	public Teleporter(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide) 
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		active = false;
	}
	public Teleporter(float size, float xPos, float yPos) 
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, false, false);
		active = false;
	}
	public  Vector2f getPos()
	{
		return new Vector2f(getXPos(),getYPos());
	}
	
	@Override
	public void interact (Entity ent)
	{
		if(active == false)
		{
			((PhysEnt) ent).setPos(TeleporterLinker.getLinkedPos(this).getX(), TeleporterLinker.getLinkedPos(this).getY());
			tempEnt = ent;
		}
	}
	
	@Override
	public void uninteract (Entity ent)		
	{
		active = false;
	}
	
	public void setActive(boolean bool)
	{
		active = bool;
	}
}
