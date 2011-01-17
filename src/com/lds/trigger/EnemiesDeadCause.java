package com.lds.trigger;

import com.lds.game.Enemy;

public class EnemiesDeadCause 
{
	public EnemiesDeadCause ()
	{
		super();
	}

	public boolean isTriggered ()
	{
		if (Enemy.getEnemyCount() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
