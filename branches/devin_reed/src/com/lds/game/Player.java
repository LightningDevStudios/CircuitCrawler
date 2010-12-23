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
	
	@Override
	public void interact (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13); //gets the type of class
		
		if (entType.equals("Player"))
		{
			stop();
			colList.remove(ent);
		}
	}
	
	//this method may be neccessary in the future
	/*@Override
	public void uninteract (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13);
	}*/
}
