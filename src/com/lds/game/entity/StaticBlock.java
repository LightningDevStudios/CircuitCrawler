package com.lds.game.entity;

public class StaticBlock extends StaticEnt
{
	public StaticBlock(float size, float xPos, float yPos)
	{
		super(size, xPos, yPos, false, true);
	}
	public StaticBlock (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid)
	{
		super(size, xPos, yPos, 0.0f, xScl, yScl, isSolid, false, true);
	}
	
	@Override
	public void interact (Entity ent)
	{
		colList.remove(ent);
	}
}
