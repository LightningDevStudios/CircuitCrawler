package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Vector2f;
import com.lds.Stopwatch;
import com.lds.game.Game;

public class Cannon extends StaticEnt
{
	protected float speed, cannonXPos, cannonYPos;
	protected int time;
	
	public Cannon(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float fireSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		speed = fireSpeed;
		cannonXPos = xPos;
		cannonYPos = yPos;
		time = Stopwatch.elapsedTimeMs();		
	}

	@Override
	public void update()
	{
		super.update();
		if(Stopwatch.elapsedTimeMs() - time > 5000)
		{
			time = Stopwatch.elapsedTimeMs();
			PhysBall cannonShot = new PhysBall(15, cannonXPos, cannonYPos);
			cannonShot.enableTilesetMode(Game.tilesetwire, 1, 2);
			cannonShot.push(new Vector2f(angle).scale(speed));
			EntityManager.addEntity(cannonShot);
		}
	}
}
