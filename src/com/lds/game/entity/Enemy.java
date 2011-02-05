package com.lds.game.entity;

import com.lds.EntityManager;

public abstract class Enemy extends Character //enemies will fall under this class
{
	private static int enemyCount = 0;

	public Enemy(float size, float xPos, float yPos, boolean circular, int health)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, circular, health);
	}
	
	public Enemy(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean circular, int health)
	{
		super(size, xPos, yPos, angle, xScl, yScl, circular, health, 0.0f);
		enemyCount++;
	}
	
	public void die ()
	{
		enemyCount--;
		EntityManager.removeEntity(this);
	}
	
	public static int getEnemyCount ()
	{
		return enemyCount;
	}
}