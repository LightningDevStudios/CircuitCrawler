package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

public class Block extends HoldObject
{	
    public Block(float size, Vector2 position)
    {
        super(new Rectangle(new Vector2(size, size), position, true));
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
}
