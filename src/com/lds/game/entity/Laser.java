package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.math.Vector2;

public class Laser extends Entity
{
    public Laser(float size, float angle, Vector2 position)
    {
        super(new com.lds.physics.primitives.Rectangle(new Vector2(size, size), position, angle, true));
    }
    
    @Override
    public void interact(Entity ent)
    {
        if(ent instanceof Player)
        {
            EntityManager.removeEntity(ent);
        }
    }
}
