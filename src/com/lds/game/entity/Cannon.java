package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Vector2f;
import com.lds.Stopwatch;
import com.lds.game.Game;

public class Cannon extends StaticEnt
{
	protected float speed, shotsPerSecond;
	protected int time;
	
	public Cannon(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float fireSpeed, float newShotsPerSecond)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		speed = fireSpeed;
		time = Stopwatch.elapsedTimeMs();
		shotsPerSecond = newShotsPerSecond;
	}

	@Override
	public void update()
	{
		super.update();
		if(Stopwatch.elapsedTimeMs() - time > shotsPerSecond * 1000) // Time loop
		{
			time = Stopwatch.elapsedTimeMs();
			CannonShell cannonShot = new CannonShell(getXPos(), getYPos(), angle, speed, 2);
			cannonShot.enableTilesetMode(Game.tilesetwire, 1, 3);
			cannonShot.push(new Vector2f(angle).scale(speed));
			EntityManager.addEntity(cannonShot);
		}
	}
}
