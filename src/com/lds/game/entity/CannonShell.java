package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.Vector2f;
import com.lds.game.Game;

public class CannonShell extends PhysEnt //A Large Ball of Doom
{	
	protected int time;
	public CannonShell(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction)
	{
		
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
		time = Stopwatch.elapsedTimeMs();
		
	}
	@Override
	public void update()
	{
		super.update();
		if(Stopwatch.elapsedTimeMs() - time > 5000) // Time loop
		{
			time = Stopwatch.elapsedTimeMs();
			this.remove();
		}
	}
}
