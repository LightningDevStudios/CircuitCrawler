package com.lds.trigger;

import com.lds.game.entity.Enemy;

public class CauseEnemyCount extends Cause
{
	
	private int count;
	
	public CauseEnemyCount(int count)
	{
		super();
		this.count = count;
	}

	@Override
	public void update()
	{
		if (Enemy.getEnemyCount() == count)
			trigger();

		else
			untrigger();
	}
}
