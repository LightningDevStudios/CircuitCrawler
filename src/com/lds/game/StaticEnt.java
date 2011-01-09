package com.lds.game;

import com.lds.Enums.RenderMode;

public abstract class StaticEnt extends Entity //static obejcts are immovable, such as interactive switches and devices and immovable blocks
{
	public StaticEnt(float size, float xPos, float yPos, RenderMode renderMode)
	{
		super(size, xPos, yPos, renderMode);
	}
	
	public StaticEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, renderMode);
	}
}
