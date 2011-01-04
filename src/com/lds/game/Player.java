package com.lds.game;

import java.util.ArrayList;

import com.lds.EntityCleaner;

public class Player extends Character //your character, protagonist
{
	private int energy;
	private boolean holdingObject;
	
	public Player (float _xPos, float _yPos, float _angle)
	{
		//initialize Character and Entity data
		super(100, 100, PhysEnt.DEFAULT_SPEED, Entity.DEFAULT_SIZE, _xPos, _yPos, _angle, 1.0f, 1.0f);
		
		//initialize Player data
		energy = 100;
		boolean holdingObject = false;
	}
	
	public void attack ()
	{
		energy -= 5;
	}
	
	public void buttonInteract (Entity ent)
	{
		if (ent instanceof PhysEnt)
		{
			//TODO: pickup object
		}
	}
	
	@Override
	public void interact (Entity ent)
	{
		if (ent instanceof StaticEnt)
		{
			stop();
			colList.remove(ent);
		}
		
		else if (ent instanceof InvenPickup)
		{
			Inventory.add(((InvenPickup)ent).getName());
			EntityCleaner.queueEntityForRemoval(ent);
			colList.remove(ent);
		}
		else if (ent instanceof Health)
		{
			health += ((Powerup)ent).getValue();
		}
		else if (ent instanceof Energy)
		{
			energy += ((Powerup)ent).getValue();
		}
	}
	
	public boolean isHoldingObject ()
	{
		return holdingObject;
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
