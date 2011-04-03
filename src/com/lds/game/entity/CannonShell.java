package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.Vector2f;
import com.lds.game.Game;

public class CannonShell extends PhysEnt //A Large Ball of Doom
{	
	protected int time, remove;
	
	public CannonShell(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction, int secondsUntilRemove)
	{
		super(size, xPos, yPos, angle, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
		time = Stopwatch.elapsedTimeMs();
		remove = secondsUntilRemove;
	}
	
	public CannonShell(float xPos, float yPos, float angle, float friction, int secondsUntilRemove)
	{
		super(15, xPos, yPos, angle, 1.0f, 1.0f, true, false, true, 1.0f, 0.0f, 0.0f, friction);
		time = Stopwatch.elapsedTimeMs();
		remove = secondsUntilRemove;
	}
	
	@Override
	public void update()
	{
		super.update();
		if(Stopwatch.elapsedTimeMs() - time > remove * 1000) // Time loop are cools
		{
			time = Stopwatch.elapsedTimeMs();
			this.remove();
		}
	}
}
