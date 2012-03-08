package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class CannonShell extends Entity
{	
	/**
	 * @param position Position.
	 * @param angle The shell's angle in radians.
	 */
	public CannonShell(Vector2 position, float angle)
	{
		super(new Circle(15, position, angle, true));
		
		shape.setKineticFriction(0);
        shape.setStaticFriction(0);
		
		this.tilesetX = 2;
		this.tilesetY = 0;
	}
	
	@Override
	public void interact(Entity ent)
    {
	    
    }	
	
	@Override
	public void update(GL11 gl)
	{
	    
	}
}
