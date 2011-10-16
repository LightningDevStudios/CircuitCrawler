package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class CannonShell extends Entity
{	
	private int time;
	private int removeTime;
	
	/**
	 * \todo add real physics
	 */
	public CannonShell(Vector2 position, float angle, float moveSpeed, int removeTime)
	{
		super(new Circle(15, position, angle, true));
		
	}
	
	@Override
	public void update()
	{
		super.update();
		
		time += Stopwatch.getFrameTime();
		
		if (time > removeTime * 1000)
		{
			time = 0;
			EntityManager.removeEntity(this);
		}
	}
}
