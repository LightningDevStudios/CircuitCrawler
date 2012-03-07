package com.ltdev.cc.entity;

import java.util.ArrayList;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.Game;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class Cannon extends Entity
{
	private float shotVelocity, stupidity, time;
	private Player player;
	private float size;
	private ArrayList<CannonShell> shells;
	
	public Cannon(float size, Vector2 position, float angle, float stupidity, float shotVelocity, Player p)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        
        this.shotVelocity = shotVelocity;
        this.size = size;
        
        shape.isStatic = true;
        player = p;
        shells = new ArrayList<CannonShell>();
        
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
	
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
		
		if(time > 2000)
		{
		    if(shells.size() > 4)
		    {
		        EntityManager.removeEntity(shells.get(0));
		        shells.remove(0);
		    }
		    
		    CannonShell shell = new CannonShell(Vector2.add(getPos(), new Vector2((float)Math.cos(shape.getAngle()) * (size / 2 + 10), (float)Math.sin(shape.getAngle()) * (size / 2 + 10))), shape.getAngle());
            shell.setTexture(Game.tilesetentities);      
            EntityManager.addEntity(shell);
            
		    shells.add(shell);
		    shell.shape.addImpulse(new Vector2((float)Math.cos(shape.getAngle()) * shotVelocity, (float)Math.sin(shape.getAngle()) * shotVelocity));
		    
		    time = 0;
		}
	}
}
