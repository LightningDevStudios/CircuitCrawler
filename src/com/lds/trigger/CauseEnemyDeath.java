package com.lds.trigger;

import com.lds.game.entity.Enemy;

public class CauseEnemyDeath extends Cause
{
	Enemy enemy;
	
	public CauseEnemyDeath(Enemy enemy)
	{
		super();
		this.enemy = enemy;
	}
	
	@Override
	public void update()
	{
		if (enemy.isDead())
			trigger();
		
		else
			untrigger();
	}
}
