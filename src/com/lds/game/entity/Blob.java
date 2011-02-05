package com.lds.game.entity;

public class Blob extends Enemy 
{
	public Blob (float xPos, float yPos)
	{
		super(Entity.DEFAULT_SIZE, xPos, yPos, false, 100);
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (ent instanceof AttackBolt)
			health -= 75;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if (health <= 0)
			this.die();
	}
}
