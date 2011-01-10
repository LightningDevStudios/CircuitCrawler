package com.lds.game;

import com.lds.Enums.RenderMode;

public abstract class Character extends PhysEnt //all characters, including the protangonist and enemies
{
	protected int health;
	protected int strength;
	public float speed = 1.0f;
	
	public Character(float size, float xPos, float yPos, RenderMode renderMode, int health, int strength, float speed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, renderMode, health, strength, speed);
	}
	
	public Character(float size, float xPos, float yPos, float angle, float xScl, float yScl, RenderMode renderMode, int health, int strength, float speed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, RenderMode.TILESET, 0.0f, 0.0f, speed);
		
		this.health = health;
		this.strength = strength;
	}
		
	@Override
	public void update()
	{
		
	}
	public int getHealth ()
	{
		return health;
	}
	
	public int getStrength ()
	{
		return strength;
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
