package com.lds.game;

public class PhysBlock extends HoldObject //a default block
{	
	public PhysBlock (float _xPos, float _yPos)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
		rotSpeed = 3.0f;
		moveSpeed = 1.0f;
	}
}
