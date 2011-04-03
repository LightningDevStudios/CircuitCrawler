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
	public static boolean godMode, noclip;
	protected Context context;
	
	public Player (float xPos, float yPos, float angle)
	{
		//initialize Character and Entity data
		super(Entity.DEFAULT_SIZE, xPos, yPos, angle, 1.0f, 1.0f, true, HEALTH_LIMIT, 30.0f, 1.0f);
		//initialize Player data
		energy = ENERGY_LIMIT;
		nextAngle = angle;
		controlled = true;
		if(godMode)
		{
			health = 9999999;//LOLS
			energy = 9999999;//LOLS Again
		}
		if(noclip)
		{
			this.isSolid = false;
		}
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
			colList.remove(ent);
			energy += ((PickupEnergy)ent).getEnergyValue();
			if (energy > ENERGY_LIMIT)
				energy = ENERGY_LIMIT;
		}
		else if (ent instanceof PickupHealth)
		{
			colList.remove(ent);
			health += ((PickupHealth)ent).getHealthValue();
			if (health > HEALTH_LIMIT)
				health = HEALTH_LIMIT;
		}
		else if (ent instanceof Enemy || ent instanceof Spike)
		{
			takeDamage(25);
		}
		else if (ent instanceof SpikeBall)
		{
			takeDamage(25);
		}
		else if (ent instanceof CannonShell)
		{
			takeDamage(5);
		}
	}
	
	@Override
	public void onTileInteract(Tile tile)
	{
		if (tile != null)
		{
			if (tile.isPit())
			{
				this.disableUserControl();
				if (!falling)
				{
					this.stop();
					this.scaleTo(0, 0);
					this.moveTo(tile.getXPos(), tile.getYPos());
					SoundPlayer.getInstance().playSound(SoundPlayer.PIT_FALL);
				}
				falling = true;
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
			hObj.setPos(directionVec);
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
