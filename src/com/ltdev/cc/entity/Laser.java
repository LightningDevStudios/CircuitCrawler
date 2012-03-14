package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Laser is an object that reflects off mirrors and pushes everything else aside.
 * @author Lightning Development Studios
 */
public class Laser extends Entity
{
    private int countdown;
    
    /**
     * Initializes a new instance of the Laser class.
     * @param beamWidth The laser beam's width.
     * @param beamLength The laser beam's length.
     * @param angle The laser beam's angle.
     * @param position The geometric center of the laser beam.
     */
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new Rectangle(new Vector2(beamLength, beamWidth), position, (float)Math.toDegrees(angle), false));
        this.tilesetX = 1;
        this.tilesetY = 3;
        countdown = 100;
    }
    
    @Override
    public void interact(Entity ent)
    {
        super.interact(ent);
        
        /*if (ent instanceof Player)
        {
            //Player.kill();
        }*/
    }
    
    @Override
    public void update(GL11 gl)
    {
        countdown -= Stopwatch.getFrameTime();
        
        if (countdown < 0)
            EntityManager.removeEntity(this);
    }
}
