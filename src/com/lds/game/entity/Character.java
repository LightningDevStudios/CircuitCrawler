package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Stopwatch;

public abstract class Character extends PhysEnt //all characters, including the protangonist and enemies
{
	protected int health;
	protected boolean isFlashing, doneRotating;
	protected int msPassed;
	
	public Character(float size, float xPos, float yPos, boolean circular, int health, float moveSpeed, float rotSpeed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, circular, health, moveSpeed, rotSpeed);
	}
	
	public Character(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean circular, int health, float moveSpeed, float rotSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, circular, true, moveSpeed, rotSpeed, 1.0f, 0.0f);
		this.enableColorMode(1.0f, 1.0f, 1.0f, 1.0f);
		isFlashing = false;
		msPassed = Stopwatch.elapsedTimeMs();
		this.health = health;
		doneRotating = false;
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (ent instanceof AttackBolt && !((AttackBolt)ent).doesIgnore(this))
		{
			colList.remove(ent);
			takeDamage(30);
		}
		else if (ent instanceof SpikeBall)
		{
			takeDamage(25);
		}
		else if (ent instanceof CannonShell)
		{
			takeDamage(5);
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (isFlashing)
		{
			if (Stopwatch.elapsedTimeMs() - msPassed <= 250)
				this.enableColorMode(1.0f, 0.0f, 0.0f, 1.0f);
			else
			{
				this.enableColorMode(1.0f, 1.0f, 1.0f, 1.0f);
				isFlashing = false;
			}
		}
		if(this.getXScl() <= 0 && this.getYScl() <= 0)
			health = 0;
		if (health <= 0)
			this.die(); 
	}
	
	public int getHealth ()
	{
		return health;
	}
	
	public void takeDamage (int damage)
	{
		health -= damage;
		isFlashing = true;
		msPassed = Stopwatch.elapsedTimeMs();
	}
	
	public void die ()
	{
		 EntityManager.removeEntity(this);
	}
	
	public boolean isDoneRotating()
	{
		return doneRotating;
	}
	
	public void setDoneRotating(boolean doneRotating)
	{
		this.doneRotating = doneRotating;
	}
}
