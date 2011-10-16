package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.game.Game;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Cannon extends Entity
{
	private float shotVelocity;
	private float shotsPerSecond;
	private long time;
	
	public Cannon(Vector2 position, float angle, float shotVelocity, float shotsPerSecond)
	{
		this(69, position, angle, shotVelocity, shotsPerSecond);
	}
	
	public Cannon(float size, Vector2 position, float angle, float shotVelocity, float shotsPerSecond)
    {
        super(new Rectangle(size, position, angle, true));
        this.shotVelocity = shotVelocity;
        this.shotsPerSecond = shotsPerSecond;
        time = 0;
    }

	@Override
	/**
	 * \todo add real physics
	 */
	public void update()
	{
		super.update();
		
		time += Stopwatch.getFrameTime();
		
		if (time > shotsPerSecond * 1000) // Time loop
		{
			time = 0;
			CannonShell cannonShot = new CannonShell(getPos(), getAngle(), shotVelocity, 2);
			cannonShot.enableTilesetMode(Game.tilesetwire, 1, 3);
			//cannonShot.push(new Vector2(angle).scale(shotVelocity));
			EntityManager.addEntity(cannonShot);
		}
	}
}
