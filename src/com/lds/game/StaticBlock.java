package com.lds.game;

public class StaticBlock extends StaticEnt
{
	public StaticBlock (float _xPos, float _yPos, float _xScl, float _yScl)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, _xScl, _yScl);
	}
	
	@Override
	public void interact (Entity ent)
	{
		colList.remove(ent);
	}
}
