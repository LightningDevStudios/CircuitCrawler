package com.lds.game.event;

import com.lds.game.entity.Entity;
import com.lds.game.entity.Tile;

public interface InteractListener 
{
    void interact(Entity ent);
    void uninteract(Entity ent);
    void tileInteract(Tile t);
    void tileUninteract(Tile t);
}