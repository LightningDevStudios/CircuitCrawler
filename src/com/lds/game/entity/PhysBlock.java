package com.lds.game.entity;

public class PhysBlock extends HoldObject //a default block
{	
	public PhysBlock (float size, float xPos, float yPos)
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, false, 0.03f);
	}
}
