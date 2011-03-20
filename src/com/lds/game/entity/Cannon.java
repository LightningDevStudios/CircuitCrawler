package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Vector2f;
import com.lds.Stopwatch;
import com.lds.game.Game;

public class Cannon extends StaticEnt
{
	protected float speed, xPos, yPos;
	protected int time;
	
	public Cannon(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float speed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		this.speed = speed;
		this.xPos = xPos;
		this.yPos = yPos;
		time = Stopwatch.elapsedTimeMs();		
	}

	@Override
	public void update()
	{
		super.update();
		if(Stopwatch.elapsedTimeMs() - time > 5000)
		{
			time = Stopwatch.elapsedTimeMs();
			PhysBall cannonShot = new PhysBall(15, xPos, yPos);
			cannonShot.enableTilesetMode(Game.tilesetwire, 1, 2);
			cannonShot.push(new Vector2f(angle).scale(speed));
			EntityManager.addEntity(cannonShot);
		}
	}
}
