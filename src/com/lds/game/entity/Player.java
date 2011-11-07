package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL11;

import com.lds.EntityManager;
import com.lds.Stopwatch;
import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class Player extends Entity
{
	public static final int ENERGY_LIMIT = 100;
	public static final int HEALTH_LIMIT = 100;
	
	private int health;
	private int energy;
	private HoldObject hObj;
	private boolean controlled;
	private int damageTime;
	
	public Player(Vector2 position, float angle)
	{
		super(new Circle(DEFAULT_SIZE, position, angle, true));
		shape.setFriction(1);
		
		energy = ENERGY_LIMIT;
		health = HEALTH_LIMIT;
		controlled = true;
		damageTime = 0;
		
		this.tilesetX = 0;
		this.tilesetY = 2;
	}
	
	public void attack()
	{
		energy -= 5;
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (ent instanceof PickupEnergy)
		{
			energy += ((PickupEnergy)ent).getValue();
			if (energy > ENERGY_LIMIT)
				energy = ENERGY_LIMIT;
		}
		else if (ent instanceof PickupHealth)
		{
			health += ((PickupHealth)ent).getValue();
			if (health > HEALTH_LIMIT)
				health = HEALTH_LIMIT;
		}
		else if (ent instanceof SpikeBall)
		{
			takeDamage(25);
		}
		else if (ent instanceof CannonShell)
		{
			takeDamage(5);
		}
		else if (ent instanceof AttackBolt && this != ((AttackBolt)ent).getParent())
		{
			takeDamage(5);
		}
	}
	
	/**
	 * \todo fall into pits.
	 */
	@Override
	public void tileInteract(Tile tile)
	{
		/*if (tile != null)
		{
			if (tile.isPit())
			{
				this.disableUserControl();
				if (!falling)
				{
					this.stop();
					this.scaleTo(0, 0);
					this.moveTo(tile.getXPos(), tile.getYPos());
					SoundPlayer.playSound(SoundPlayer.PIT_FALL);
				}
				falling = true;
			}
		}*/
	}
	
	public void holdObject(HoldObject hObj)
	{
		this.hObj = hObj;
		hObj.hold();
		updateHeldObjectPosition();
	}
	
	public void dropObject()
	{
		hObj.drop();
		hObj = new Block(0, new Vector2(0, 0));
		hObj = null;
	}
	
	/**
	 * \todo actually push the object with physics.
	 */
	public void throwObject()
	{
		dropObject();
	}

	/**
	 * \todo do this with physics.
	 */
	public void updateHeldObjectPosition()
	{
		/*float heldDistance = hObj.halfSize * hObj.getXScl() + this.halfSize + 10.0f;
		Vector2 directionVec = new Vector2(angle);
		directionVec.scale(heldDistance).add(posVec);
		hObj.setPos(directionVec);
		hObj.setAngle(angle);*/
	}
	
	/**
	 * \todo make player flash once it is hit.
	 */
	@Override
    public void update(GL11 gl)
    {
        super.update(gl);
        
        damageTime += Stopwatch.getFrameTime();
        
        if (shape.getScale().x() <= 0 && shape.getScale().x() <= 0)
            health = 0;
        if (health <= 0)
            EntityManager.removeEntity(this); 
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public void takeDamage(int damage)
    {
        health -= damage;
        damageTime = 0;
    }
	
	public void disableUserControl()
	{
		if (hObj != null)
			dropObject();
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
	
	public boolean isHoldingObject()
	{
	    return hObj != null;
	}

	public int getEnergy()
	{
		return energy;
	}
	
	public void loseEnergy(int energyLost)
	{
		energy -= energyLost;
	}

    public void addImpulse(Vector2 f)
    {
        shape.addImpulse(f);
    }
}
