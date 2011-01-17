package com.lds.game;

import com.lds.EntityCleaner;
import com.lds.Enums.RenderMode;

public class Player extends Character //your character, protagonist
{
	private int energy;
	private boolean holdingObject;
	private HoldObject hObj;
	
	public Player (float xPos, float yPos, float angle, RenderMode renderMode)
	{
		//initialize Character and Entity data
		super(Entity.DEFAULT_SIZE, xPos, yPos, angle, 1.0f, 1.0f, renderMode, 100, 100, 0.0f);
		
		//initialize Player data
		energy = 100;
	}
	
	@Override
	public void update()
	{
		super.update();
		if (holdingObject)
		{
			//updateHeldObjectPosition();
		}
	}
	
	public void die ()
	{
		EntityCleaner.queueEntityForRemoval(this);
	}
	
	public void attack ()
	{
		energy -= 5;
	}
	
	@Override
	public void interact (Entity ent)
	{
		if (ent instanceof StaticBlock || ent instanceof HoldObject || ent instanceof Door)
		{
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
	
	public HoldObject getHeldObject()
	{
		return hObj;
	}

	public int getEnergy()
	{
		return energy;
	}

	public boolean isHoldingObject()
	{
		return holdingObject;
	}
	
	public void holdObject(HoldObject hObj)
	{
		holdingObject = true;
		this.hObj = hObj;
		hObj.hold();
		updateHeldObjectPosition();
		colIgnoreList.add(hObj);
		hObj.colIgnoreList.add(this);
	}
	
	public void dropObject()
	{
		holdingObject = false;
		colIgnoreList.remove(hObj);
		hObj.colIgnoreList.remove(this);
		hObj.drop();
		hObj = new PhysBlock(0.0f, 0.0f, 0.0f, RenderMode.BLANK);
		hObj = null;
	}

	public void updateHeldObjectPosition()
	{
		float heldDistance = halfSize + hObj.halfSize + 10.0f;
		initializeCollisionVariables();
		hObj.setPos((float)Math.cos(rad) * heldDistance + xPos, (float)Math.sin(rad) * heldDistance + yPos);
		hObj.setAngle(angle);
	}
}
