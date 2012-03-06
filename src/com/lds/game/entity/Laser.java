package com.lds.game.entity;

import com.lds.physics.primitives.*;
import com.lds.math.Vector2;

public class Laser extends Entity
{
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new Rectangle(new Vector2(beamWidth, beamLength), position, angle, true));
    }
    
    @Override
    public void interact(Entity ent)
    {
        if(ent instanceof Player)
        {
         
        }
    }
}
