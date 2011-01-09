package com.lds.game;

import com.lds.Enums.RenderMode;

public abstract class Character extends PhysEnt //all characters, including the protangonist and enemies
{
	protected int health;
	protected int strength;
	
	public Character(float size, float xPos, float yPos, RenderMode renderMode, int health, int strength)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, renderMode, health, strength);
	}
	
	public Character(float size, float xPos, float yPos, float angle, float xScl, float yScl, RenderMode renderMode, int health, int strength)
	{
		super(size, xPos, yPos, angle, xScl, yScl, true, RenderMode.TILESET, 0.0f, 0.0f, 0.0f);
		
		this.health = health;
		this.strength = strength;
	}
		
	public int getHealth ()
	{
		return health;
	}
	
	public int getStrength ()
	{
		return strength;
	}
	
	public void takeDamage (int damage)
	{
		health -= damage;
	}
	
	public void die ()
	{
		 
	}
}