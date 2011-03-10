package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Vector2f;
import com.lds.Stopwatch;

public class Cannon extends StaticEnt
{
	protected float speed, cannonXPos, cannonYPos, fireAngle;
	protected int time;
	protected PhysBall cannonShot = new PhysBall(15, cannonXPos, cannonYPos);
	
	public Cannon(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float fireSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		speed = fireSpeed;
		cannonXPos = xPos;
		cannonYPos = yPos;
		fireAngle = angle;
		time = Stopwatch.elapsedTimeMs();
	}

	@Override
	public void update()
	{
		super.update();
		cannonShot.push(new Vector2f(fireAngle).scale(speed));
		if(Stopwatch.elapsedTimeMs() - time > 5000)
		{
			time = Stopwatch.elapsedTimeMs();
			//EntityManager.removeEntity(cannonShot);
			cannonShot = new PhysBall(15, cannonXPos, cannonYPos);
		}
		
	}
	
}
