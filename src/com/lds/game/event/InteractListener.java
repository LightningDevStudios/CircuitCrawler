package com.lds.game.event;

import com.lds.game.Tile;
import com.lds.game.entity.Entity;

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
     * Called when a Shape collides, or "interacts", with a Tile.
     * @param t The Tile that the Shape collided with.
     */
    void tileInteract(Tile t);
    
    /**
     * Called when a Shape stops colliding, or "uninteracts", with a Tile.
     * @param t The Tile that the Shape stopped colliding with.
     */
    void tileUninteract(Tile t);
    
    /**
     * Gets the Entity that implements this interface.
     * @return The containing entity.
     */
    Entity getEntity();
}
