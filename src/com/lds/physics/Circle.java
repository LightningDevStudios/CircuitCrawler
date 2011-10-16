package com.lds.physics;

import com.lds.math.Vector2;

public class Circle extends Shape 
{
    /****************
     * Constructors *
     ***************/
    
    /**
     * Initializes a new instance of the Circle class.
     */
    public Circle()
    {
        this(true);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param solid The solidity of the circle
     */
    public Circle(boolean solid)
    {
        this(1, solid);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param size The size of the circle
     * @param solid The solidity of the circle
     */
    public Circle(float size, boolean solid)
    {
        this(size, new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param size The size of the circle
     * @param position The position of the circle
     * @param solid The solidity of the circle
     */
    public Circle(float size, Vector2 position, boolean solid)
    {
        this(size, position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * @param size The size of the circle
     * @param position The position of the circle
     * @param angle The angle of the circle in radians
     * @param solid The solidity of the circle
     */
    public Circle(float size, Vector2 position, float angle, boolean solid)
    {
        this(size, position, angle, new Vector2(1, 1), solid);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param size The size of the circle
     * @param position The position of the circle
     * @param angle The angle of the circle in radians
     * @param scale The scale of the circle
     * @param solid The solidity of the circle
     */
    public Circle(float size, Vector2 position, float angle, Vector2 scale, boolean solid)
    {
        super(position, angle, scale, solid);
        
        float halfSize = size / 2;
        float[] vertices = 
            {
                halfSize, halfSize,     //top left
                halfSize, -halfSize,    //bottom left
                -halfSize, halfSize,    //top right
                -halfSize, -halfSize    //bottom right
            };
        this.vertices = vertices;
    }
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    /**
     * Gets the circle's radius.
     * @return The circle's radius.
     */
    public float getRadius()
    {
        return vertices[5] * scale.getX();
    }
    
    @Override
    public void setScale(Vector2 scale)
    {
        if (scale.getX() == scale.getY())
            super.setScale(scale);
    }
}
