package com.lds.game.entity;

public class CannonShell extends PhysEnt //A Large Ball of Doom
{	
	public CannonShell(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction)
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
	}
}
