package com.lds.game.entity;

public class PhysBall extends HoldObject //a default circular block
{	
	public PhysBall (float size, float xPos, float yPos, float friction)
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, true, friction);
	}
}
