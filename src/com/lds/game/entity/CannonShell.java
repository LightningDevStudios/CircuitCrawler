package com.lds.game.entity;

import com.lds.Stopwatch;

public class CannonShell extends PhysEnt //A Large Ball of Doom
{	
	protected int time, remove;
	
	public CannonShell(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction, int secondsUntilRemove)
	{
		super(size, xPos, yPos, angle, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
		time = Stopwatch.elapsedTimeMs();
		remove = secondsUntilRemove;
	}
	
	public CannonShell(float xPos, float yPos, float angle, float moveSpeed, int secondsUntilRemove)
	{
		this(15, xPos, yPos, angle, 1.0f, 1.0f, true, true, true, moveSpeed, 0.0f, 0.0f, 0.07f, secondsUntilRemove);
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
