package com.lds.game;

import com.lds.Enums.RenderMode;

public class StaticBlock extends StaticEnt
{
	public StaticBlock(float size, float xPos, float yPos, RenderMode renderMode)
	{
		super(size, xPos, yPos, renderMode);
	}
	public StaticBlock (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode)
	{
		super(size, xPos, yPos, 0.0f, xScl, yScl, isSolid, renderMode);
	}
	
	@Override
	public void interact (Entity ent)
	{
		colList.remove(ent);
	}
}
