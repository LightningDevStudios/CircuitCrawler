package com.lds.game.entity;

import com.lds.physics.World;

import com.lds.EntityManager;
import com.lds.math.Vector2;

public class Laser extends Entity
{
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new com.lds.physics.primatives.Rectangle(new Vector2(beamWidth, beamLength), position, angle, true));
    }
    
    @Override
    public void interact(Entity ent)
    {
        if(ent instanceof Player)
        {
         
        }
    }
}
