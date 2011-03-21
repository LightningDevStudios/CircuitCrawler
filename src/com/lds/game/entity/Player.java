package com.lds.game.entity;

import android.content.Context;
import android.os.Vibrator;

import com.lds.Vector2f;
import com.lds.game.SoundPlayer;

public class Player extends Character //your character, protagonist
{
	public static final int ENERGY_LIMIT = 100, HEALTH_LIMIT = 100;
	private int energy;
	private boolean holdingObject;
	private HoldObject hObj;
	private boolean controlled;
	private float nextAngle;
	protected Context context;
	protected boolean leavingIce;
	
	public Player (float xPos, float yPos, float angle)
	{
		//initialize Character and Entity data
		super(Entity.DEFAULT_SIZE, xPos, yPos, angle, 1.0f, 1.0f, false, HEALTH_LIMIT, 30.0f, 1.0f);
		//initialize Player data
		energy = ENERGY_LIMIT;
		nextAngle = angle;
	}
	
	public void attack ()
	{
		energy -= 5;
	}
	
	@Override
	public void interact (Entity ent)
	{
		super.interact(ent);
		if (ent instanceof PickupEnergy)
		{
			energy += ((PickupEnergy)ent).getEnergyValue();
			if (energy > ENERGY_LIMIT)
				energy = ENERGY_LIMIT;
		}
		else if (ent instanceof PickupHealth)
		{
			health += ((PickupHealth)ent).getHealthValue();
			if (health > HEALTH_LIMIT)
				health = HEALTH_LIMIT;
		}
		else if (ent instanceof Enemy || ent instanceof Spikes)
		{
			takeDamage(25);
		}
		else if (ent instanceof SpikeBall)
		{
			//vibrator(2000);
			takeDamage(25);
		} 
	}
	
	@Override
	public void onTileInteract(Tile tile)
	{
		if (tile != null)
		{
			if (tile.isPit() && controlled)
			{
				this.disableUserControl();
				this.scaleTo(0, 0);
				this.moveTo(tile.getXPos(), tile.getYPos());
				if (!falling)
					SoundPlayer.getInstance().playSound(SoundPlayer.PIT_FALL);
				falling = true;
			}
			else if (tile.isSlipperyTile())
			{
				this.disableUserControl();
				this.moveTo(moveVec);
				//leavingIce = true;
			}
			else
			{
				this.stop();
				this.enableUserControl();
			}
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
		final Vector2f addVec = new Vector2f(angle).scale(10);
		hObj.addPos(addVec);
		hObj.drop();
		hObj = new PhysBlock(0.0f, 0.0f, 0.0f, 0.03f);
		hObj = null;
	}
	
	public void throwObject()
	{
		holdingObject = false;
		colIgnoreList.remove(hObj);
		hObj.colIgnoreList.remove(this);
		final Vector2f addVec = new Vector2f(angle).scale(10);
		hObj.addPos(addVec);
		hObj.push();
		hObj = new PhysBlock(0.0f, 0.0f, 0.0f, 0.03f);
		hObj = null;
	}
	
	public void vibrator(int time)
	{
		Vibrator vibrator = null; 
		try 
		{ 
			vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE); 
		} 
		catch (Exception e) {}
		
		if (vibrator != null)
		{ 
		  try 
		  { 
			  vibrator.vibrate(((long)time)); 
		  } 
		  catch (Exception e) {} 
		} 
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
		if (ent == this.getHeldObject() || ent instanceof Tile && !((Tile)ent).isRendered())
			return false;
		return super.isColliding(ent);
	}
	
	public void disableUserControl()
	{
		if (holdingObject)
			this.dropObject();
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
	
	public float getNextAngle()
	{
		return nextAngle;
	}
	
	public void setNextAngle(float angle)
	{
		nextAngle = angle;
	}
}
