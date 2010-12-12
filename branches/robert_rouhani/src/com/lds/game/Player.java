package com.lds.game;

public class Player extends Character //your character, protagonist
{
	private int energy;
	
	public Player ()
	{
		energy = 100;
		health = 100;
		strength = 10;
		speed = 1;
	}
	
	public void attack ()
	{
		energy -= 5;
	}
}
