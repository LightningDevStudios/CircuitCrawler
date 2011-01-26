package com.lds.game.entity;

import com.lds.Enums.RenderMode;

public class PhysCircle extends HoldObject //a default circular block
{	
	public PhysCircle (float size, float xPos, float yPos, RenderMode renderMode)
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, true, renderMode);
	}
}
