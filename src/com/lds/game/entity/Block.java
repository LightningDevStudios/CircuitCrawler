package com.lds.game.entity;

import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class Block extends HoldObject
{	
    public Block(float size, Vector2 position)
    {
        super(new Rectangle(size, position, true));
    }
}
