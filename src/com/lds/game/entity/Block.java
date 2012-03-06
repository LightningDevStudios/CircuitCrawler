package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.primitives.Rectangle;

public class Block extends HoldObject
{	
    public Block(float size, Vector2 position)
    {
        super(new Rectangle(new Vector2(size, size), position, true));
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
}
