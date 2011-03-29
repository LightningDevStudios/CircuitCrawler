package com.lds.trigger;

import com.lds.game.entity.Enemy;

public class EffectSetEnemyOuterRadius extends Effect
{
	private int radius;
	
	public EffectSetEnemyOuterRadius(int radius)
	{
		this.radius = radius;
	}
	
	public void fireOutput()
	{
		Enemy.OUTER_RADIUS = radius;
	}
}
