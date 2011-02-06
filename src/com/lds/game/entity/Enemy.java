package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.game.SoundPlayer;
import com.lds.Enums.EnemyType;
import com.lds.Stopwatch;

public abstract class Enemy extends Character //enemies will fall under this class
{
	private static int enemyCount = 0;
	protected EnemyType type;
	protected int lastTime, randomTime;

	public Enemy(float size, float xPos, float yPos, boolean circular, int health, EnemyType type)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, circular, health, type);
	}
	
	public Enemy(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean circular, int health, EnemyType type)
	{
		super(size, xPos, yPos, angle, xScl, yScl, circular, health, 0.0f);
		this.type = type;
		lastTime = Stopwatch.elapsedTimeMs();
		randomTime = 500;
		enemyCount++;
	}
	
	@Override
	public void update()
	{
		super.update();
	}
	
	@Override
	public void die ()
	{
		enemyCount--;
		EntityManager.removeEntity(this);
		SoundPlayer.getInstance().playSound(3);
	}
	
	public EnemyType getType()
	{
		return type;
	}
	
	public static int getEnemyCount ()
	{
		return enemyCount;
	}
	
	public int getRandomTime()
	{
		return randomTime;
	}
	
	public int getLastTime()
	{
		return lastTime;
	}
	
	public void setRandomTime(int randomTime)
	{
		this.randomTime = randomTime;
	}
	
	public void setLastTime(int lastTime)
	{
		this.lastTime = lastTime;
	}
}