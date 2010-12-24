package com.lds.game;

public abstract class Character extends Entity //all characters, including the protangonist and enemies
{
	protected int health, strength;
	protected double speed;
	
	public Character ()
	{
		//initialize();
	}
	
	public int getHealth ()
	{
		return health;
	}
	
	public int getStrength ()
	{
		return strength;
	}
	
	public double getSpeed ()
	{
		return speed;
	}
	
	public void takeDamage (int damage)
	{
		health -= damage;
	}
	
	public void die ()
	{
		 
	}
}