package com.ltdev.cc.entity;

import javax.microedition.khronos.opengles.GL11;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.RaycastData;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

public class Laser extends Entity
{
    private int countdown;
    
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new Rectangle(new Vector2(beamWidth, beamLength), position, (float)Math.toDegrees(angle) + 90, false));
        countdown = 1000;
    }
    
    @Override
    public void interact(Entity ent)
    {
        if (ent instanceof Player)
        {
            //Player.kill();
        }
    }
    
    @Override
    public void update(GL11 gl)
    {
        countdown -= Stopwatch.getFrameTime();
        
        if (countdown < 0)
            EntityManager.removeEntity(this);
    }
}
