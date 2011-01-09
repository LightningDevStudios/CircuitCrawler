package com.lds.game;

import com.lds.Enums.RenderMode;

public class PhysBlock extends HoldObject //a default block
{	
	public PhysBlock (float size, float xPos, float yPos, RenderMode renderMode)
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, renderMode);
	}
}
