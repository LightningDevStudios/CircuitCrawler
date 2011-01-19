package com.lds.game;

import com.lds.Enums.RenderMode;
import com.lds.EntityCleaner;

public /*abstract*/ class Enemy extends Character //enemies will fall under this class
{
	private static int enemyCount = 0;

	public Enemy(float size, float xPos, float yPos, RenderMode renderMode, int health, int strength)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, renderMode, health, strength);
	}
	
	public Enemy(float size, float xPos, float yPos, float angle, float xScl, float yScl, RenderMode renderMode, int health, int strength)
	{
		super(size, xPos, yPos, angle, xScl, yScl, RenderMode.TILESET, health, strength, 0.0f);
		enemyCount++;
	}
	
	public void die ()
	{
		enemyCount--;
		EntityCleaner.queueEntityForRemoval(this);
	}
	
	public static int getEnemyCount ()
	{
		return enemyCount;
	}
}