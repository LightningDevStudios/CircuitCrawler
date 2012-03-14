package com.ltdev.cc.entity;

import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

/**
 * A Block is a basic square that can be held and thrown.
 * @author Lightning Development Studios
 */
public class Block extends HoldObject
{
    /**
     * Initializes a new instance of the Block class.
     * @param size The size of the Block.
     * @param position The position of the Block.
     */
    public Block(float size, Vector2 position)
    {
        super(new Rectangle(new Vector2(size, size), position, true));
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
}
