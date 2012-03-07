package com.ltdev.cc.event;

import com.ltdev.cc.entity.Entity;

/**
 * A delegate that allows an Entity to run some code when two Shapes interact.
 * @author Lightning Development Studios
 */
public interface InteractListener 
{
    /**
     * Called when two Shapes collide, or "interact".
     * @param ent The colliding Entity.
     */
    void interact(Entity ent);
    
    /**
     * Called when two Shapes stop colliding, or "uninteract".
     * @param ent The Entity that stopped colliding with this one.
     */
    void uninteract(Entity ent);
    
    /**
     * Gets the Entity that implements this interface.
     * @return The containing entity.
     */
    Entity getEntity();
}
