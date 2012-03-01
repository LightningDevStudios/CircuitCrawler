package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.math.Vector2;
import com.lds.physics.primatives.Rectangle;

import javax.microedition.khronos.opengles.GL11;

public class Cannon extends Entity
{
	private float shotVelocity;
	private float shotsPerSecond;
	private long time;
	private Player player;
	
	public Cannon(Vector2 position, float angle, float shotVelocity, float shotsPerSecond, Player p)
	{
		this(69, position, angle, shotVelocity, shotsPerSecond, p);
	}
	
	public Cannon(float size, Vector2 position, float angle, float shotVelocity, float shotsPerSecond, Player p)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        this.shotVelocity = shotVelocity;
        this.shotsPerSecond = shotsPerSecond;
        time = 0;
        this.player = p;
    }
	
	public void facePlayer()
	{
	    Vector2 distance = Vector2.subtract(shape.getPos(), player.getPos());
	    float angle = (float)Math.atan2(Vector2.perpDot(Vector2.UNIT_X,distance), Vector2.dot(Vector2.UNIT_X,distance));
	    shape.setAngle(angle);
	}

	@Override
	/**
	 * \todo add real physics.
	 * @param gl The OpenGL context.
	 */
	public void update(GL11 gl)
	{
		super.update(gl);
		
		time += Stopwatch.getFrameTime();
		
		if (time > shotsPerSecond * 1000) // Time loop
		{
			facePlayer();
		    time = 0;
			CannonShell cannonShot = new CannonShell(getPos(), getAngle(), shotVelocity, 2);
			EntityManager.addEntity(cannonShot);
		}
	}
}
