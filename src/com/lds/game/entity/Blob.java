package com.lds.game.entity;

import com.lds.Enums.EnemyType;

public class Blob extends Enemy 
{
	public Blob (float xPos, float yPos, EnemyType type)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, false, 100, type);
	}
}
