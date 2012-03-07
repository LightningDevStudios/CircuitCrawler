package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

public class Laser extends Entity
{
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new Rectangle(new Vector2(beamWidth, beamLength), position, angle, true));
    }
    
    @Override
    public void interact(Entity ent)
    {
        super.interact(ent);
        
        if (ent instanceof Player)
        {
         
        }
    }
}
