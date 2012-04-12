package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Cannon is an object that fires blocks at the player to push them.
 * @author Lightning Development Studios
 */
public class Cannon extends Entity
{
	private float shotVelocity, stupidity, time;
	private Player player;
	private float size;
	private ArrayList<CannonShell> shells;
	
	/**
	 * Initializes a new instance of the Cannon class.
	 * \todo Create comments for the last 3 variables.
	 * @param size The Cannon's size.
	 * @param position The Cannon's position.
	 * @param angle The Cannon's angle.
	 * @param stupidity A value that changes the cannon's accuracy.
	 * @param shotVelocity The velocity that cannons are shot at.
	 * @param p A reference to the player.
	 */
	public Cannon(float size, Vector2 position, float angle, float stupidity, float shotVelocity, Player p)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        
        this.shotVelocity = shotVelocity;
        this.size = size;
        
        shape.setStatic(true);
        player = p;
        shells = new ArrayList<CannonShell>();
        
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
	
	/**
	 * Force the Cannon to face the player.
	 */
	public void facePlayer()
	{
	    Vector2 distance = Vector2.subtract(shape.getPos(), player.getPos());
	    shape.setAngle(distance.angleDeg() + 180 + (float)(Math.random() * 2 - 1) * stupidity);
	}

	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
		facePlayer();
		
		time += Stopwatch.getFrameTime();
		
		if (time > 3000)
		{
		    if (shells.size() > 2)
		    {
		        EntityManager.removeEntity(shells.get(0));
		        shells.remove(0);
		    }
		    
		    CannonShell shell = new CannonShell(Vector2.add(getPos(), new Vector2((float)Math.cos(shape.getAngle()) * (size / 2 + 10), (float)Math.sin(shape.getAngle()) * (size / 2 + 10))), shape.getAngle());
            shell.setTexture(TextureManager.getTexture("tilesetentities"));      
            EntityManager.addEntity(shell);
            
		    shells.add(shell);
		    shell.shape.addImpulse(new Vector2((float)Math.cos(shape.getAngle()) * shotVelocity, (float)Math.sin(shape.getAngle()) * shotVelocity));
		    
		    time = 0;
		}
	}
}
