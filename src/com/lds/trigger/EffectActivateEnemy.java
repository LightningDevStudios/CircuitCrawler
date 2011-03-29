package com.lds.trigger;

import com.lds.game.entity.Enemy;

public class EffectActivateEnemy extends Effect
{
	private Enemy enemy;
	
	public EffectActivateEnemy (Enemy enemy)
	{
		this.enemy = enemy;
	}
	
	@Override
	public void fireOutput()
	{
		enemy.active = true;
	}
	
	@Override
	public void unfireOutput()
	{
		enemy.active = false;
	}
}
