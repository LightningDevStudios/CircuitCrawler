package com.lds.game.entity;

public abstract class Character extends PhysEnt //all characters, including the protangonist and enemies
{
	protected int health;
	private float speed;
	
	public Character(float size, float xPos, float yPos, boolean circular, int health, float speed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, circular, health, speed);
	}
	
	public Character(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean circular, int health, float speed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, circular, true, 20.0f, 90.0f, 1.0f);
		
		this.health = health;
		this.speed = 1.0f;
	}
		
	@Override
	public void update()
	{
		super.update();
	}
	
	public float getSpeed ()
	{
		return speed;
	}
	
	public int getHealth ()
	{
		return health;
	}
	
	public void setSpeed (float _speed)
	{
		speed = _speed;
	}
	
	public void takeDamage (int damage)
	{
		health -= damage;
	}
	
	public void die ()
	{
		 
	}
}
