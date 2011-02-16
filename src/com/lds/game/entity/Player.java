package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.Vector2f;

public class Player extends Character //your character, protagonist
{
	private int energy;
	private boolean holdingObject;
	private HoldObject hObj;
	private boolean controlled;
	
	public Player (float xPos, float yPos, float angle)
	{
		//initialize Character and Entity data
		super(Entity.DEFAULT_SIZE, xPos, yPos, angle, 1.0f, 1.0f, false, 100, 540.0f);
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
		if (ent instanceof AttackBolt)
		{
			takeDamage(5);
			//move the player back further upon collision; copy this line to make it bounce back further
			this.rectangleBounce(ent);
		}
		else if (ent instanceof PickupEnergy)
		{
			energy += ((PickupEnergy)ent).getEnergyValue();
		}
		else if (ent instanceof PickupHealth)
		{
			health += ((PickupHealth)ent).getHealthValue();
		}
		else if (ent instanceof Enemy)
		{
			takeDamage(25);
			//see above
			this.rectangleBounce(ent);
		}
		else if (ent instanceof PuzzleBox)
		{
			posVec.add(new Vector2f(-10, -10));
		}
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
		hObj = new PhysBlock(0.0f, 0.0f, 0.0f);
		hObj = null;
	}

	public void updateHeldObjectPosition()
	{
		float heldDistance = hObj.halfSize * hObj.getXScl() + this.halfSize + 10.0f;
		Vector2f directionVec = new Vector2f(angle);
		directionVec.scale(heldDistance).add(posVec);
			
		hObj.setPos(directionVec.getX(), directionVec.getY());
		hObj.setAngle(angle);
	}
	
	@Override
	public boolean isColliding(Entity ent)
	{
		if (ent == this.getHeldObject())
			return false;
		return super.isColliding(ent);
	}
	
	public void disableUserControl()
	{
		controlled = false;
	}
	
	public void enableUserControl()
	{
		controlled = true;
	}
	
	public boolean userHasControl()
	{
		return controlled;
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
	
	public void loseEnergy(int energyLost)
	{
		energy -= energyLost;
	}
}
