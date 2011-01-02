package com.lds.game;

import java.util.ArrayList;

public class Player extends Character //your character, protagonist
{
	private int energy;
	private ArrayList<String> inventory = new ArrayList<String>(); //holds the name of each pickup object in the inventory
	
	public Player (float _xPos, float _yPos, float _angle)
	{
		//initialize Character and Entity data
		super(100, 100, PhysEnt.DEFAULT_SPEED, Entity.DEFAULT_SIZE, _xPos, _yPos, _angle, 1.0f, 1.0f);
		
		//initialize Player data
		energy = 100;
	}
	
	public void attack ()
	{
		energy -= 5;
	}
	
	@Override
	public void interact (Entity ent)
	{
		if (ent instanceof Player || ent instanceof StaticEnt)
		{
			stop();
			colList.remove(ent);
		}
		
		else if (ent instanceof PickupObj)
		{
			inventory.add("test");
			colList.remove(ent);
		}
		/*
		String superType = ent.getClass().getSuperclass().getName().substring(13); //gets the type of superclass
		String entType = ent.getClass().getName().substring(13); //gets the type of class
		
		if (entType.equals("Player") || entType.equals("StaticBlock"))
		{
			stop();
			colList.remove(ent);
		}
		else if (superType.equals("PickupObj"))
		{
			inventory.add(entType);
			colList.remove(ent);
		}*/
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	//this method may be neccessary in the future
	/*@Override
	public void uninteract (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13);
	}*/
}
