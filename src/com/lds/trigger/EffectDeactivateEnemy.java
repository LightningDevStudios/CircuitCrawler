package com.lds.trigger;

import com.lds.game.entity.Enemy;

public class EffectDeactivateEnemy extends Effect
{
	private Enemy enemy;
	
	public EffectDeactivateEnemy (Enemy enemy)
	{
		this.enemy = enemy;
	}
	
	@Override
	public void fireOutput()
	{
		enemy.active = false;
	}
	
	@Override
	public void unfireOutput()
	{
		enemy.active = true;
	}
}
