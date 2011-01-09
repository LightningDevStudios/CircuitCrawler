package com.lds.game;

import com.lds.EntityCleaner;
import com.lds.Enums.RenderMode;

public class Player extends Character //your character, protagonist
{
	private int energy;
	private boolean holdingObject;
	
	public Player (float xPos, float yPos, float angle, RenderMode renderMode)
	{
		//initialize Character and Entity data
		super(Entity.DEFAULT_SIZE, xPos, yPos, angle, 1.0f, 1.0f, renderMode, 100, 100);
		
		//initialize Player data
		energy = 100;
	}
	
	public void attack ()
	{
		energy -= 5;
	}
	
	public void buttonInteract (Entity ent)
	{
		
	}
	
	@Override
	public void interact (Entity ent)
	{
		if (ent instanceof StaticEnt)
		{
			stop();
			colList.remove(ent);
		}
		else if (ent instanceof HoldObject)
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

	public int getEnergy()
	{
		return energy;
	}
	
	public boolean isHoldingObject()
	{
		return holdingObject;
	}
	
	public void holdObject()
	{
		holdingObject = true;
	}
	
	public void dropObject()
	{
		holdingObject = false;
	}
	
	//this method may be neccessary in the future
	/*@Override
	public void uninteract (Entity ent)
	{
		String entType = ent.getClass().getName().substring(13);
	}*/
}
