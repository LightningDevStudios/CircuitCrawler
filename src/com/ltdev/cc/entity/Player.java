package com.ltdev.cc.entity;

import com.ltdev.cc.Tile;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class Player extends Entity
{
	private HoldObject hObj;
	private boolean controlled;
	
	public Player(Vector2 position, float angle)
	{
		super(new Circle(DEFAULT_SIZE, position, angle, true));
		controlled = true;
		shape.setStaticFriction(2);
		shape.setKineticFriction(5);
	}
	
	
	@Override
	public void interact(Entity ent)
	{
				    
	}
	
	/**
	 * \todo fall into pits.
	 * @param tile The tile to interact with.
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
	 * @param gl The OpenGL context.
	 */
	@Override
    public void update(GL11 gl)
    {
        super.update(gl); 
    }
	
	public static void kill()
	{
	    System.out.println("LOLZ PLAYERZ ARE DETH");
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

    public void addImpulse(Vector2 f)
    {
        shape.addImpulse(f);
    }
}
