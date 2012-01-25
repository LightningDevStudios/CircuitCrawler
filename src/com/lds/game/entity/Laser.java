package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Laser extends Entity
{
    public Laser(float size, float angle, Vector2 position)
    {
        super(new Rectangle(size, position, angle, true));
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
