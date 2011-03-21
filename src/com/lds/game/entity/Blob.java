package com.lds.game.entity;

import com.lds.Enums.AIType;

public class Blob extends Enemy 
{
	public Blob (float xPos, float yPos, AIType type)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, false, 25, type);
	}
}
