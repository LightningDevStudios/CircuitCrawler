package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.game.Game;
import com.lds.math.Vector2;

public class Cannon extends StaticEnt
{
	protected float speed, shotsPerSecond;
	protected long time;
	
	public Cannon(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float fireSpeed, float newShotsPerSecond)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		speed = fireSpeed;
		shotsPerSecond = newShotsPerSecond;
		time = 0;
	}

	@Override
	public void update()
	{
		super.update();
		
		time += Stopwatch.getFrameTime();
		
		if(time > shotsPerSecond * 1000) // Time loop
		{
			time = 0;
			CannonShell cannonShot = new CannonShell(getXPos(), getYPos(), angle, speed, 2);
			cannonShot.enableTilesetMode(Game.tilesetwire, 1, 3);
			cannonShot.push(new Vector2(angle).scale(speed));
			EntityManager.addEntity(cannonShot);
		}
	}
}
