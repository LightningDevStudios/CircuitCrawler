package com.lds.game;

public abstract class Character extends Entity //all characters, including the protangonist and enemies
{
	/**
	 * @uml.property  name="health"
	 */
	protected int health;
	/**
	 * @uml.property  name="strength"
	 */
	protected int strength;
	
	public Character (int _health, int _strength, float  _speed, float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		//initialize Entity variables
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, false, true, _speed);
		
		//initialize Character variables
		health = _health;
		strength = _strength;
	}
	
	/**
	 * @return
	 * @uml.property  name="health"
	 */
	public int getHealth ()
	{
		return health;
	}
	
	/**
	 * @return
	 * @uml.property  name="strength"
	 */
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