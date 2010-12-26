package com.lds.game;

public abstract class Character extends Entity //all characters, including the protangonist and enemies
{
	protected int health, strength;
	
	public Character (int _health, int _strength, float  _speed, float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		//initialize Entity variables
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, false, true, _speed);
		
		//initialize Character variables
		health = _health;
		strength = _strength;
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