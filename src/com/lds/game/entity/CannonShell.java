package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL11;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class CannonShell extends Entity
{	
	private int time;
	private int removeTime;
	
	/**
	 * \todo add real physics.
	 * @param position Position.
	 * @param angle Angle, in radians.
	 * @param moveSpeed the Speed to interpolate movement at.
	 * @param removeTime The amount of time this CannonShell can exist for, in seconds.
	 */
	public CannonShell(Vector2 position, float angle, float moveSpeed, int removeTime)
	{
		super(new Circle(15, position, angle, true));
		
		this.tilesetX = 1;
		this.tilesetY = 3;
	}
	
	
	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
		
		time += Stopwatch.getFrameTime();
		
		if (time > removeTime * 1000)
		{
			time = 0;
			EntityManager.removeEntity(this);
		}
	}
}
