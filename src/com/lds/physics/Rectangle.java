package com.lds.physics;

import com.lds.math.Vector2;

public class Rectangle extends Shape 
{
    /****************
     * Constructors *
     ***************/
    
    /**
     * Initializes a new instance of the Rectangle class.
     */
    public Rectangle()
    {
        this(true);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param solid The solidity of the rectangle
     */
    public Rectangle(boolean solid)
    {
        this(1, solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(float size, boolean solid)
    {
        this(size, new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(float size, Vector2 position, boolean solid)
    {
        this(size, position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param angle The angle of the rectangle in radians
     * @param solid The solidity of the rectangle
     */
    public Rectangle(float size, Vector2 position, float angle, boolean solid)
    {
        this(size, position, angle, new Vector2(1, 1), solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param angle The angle of the rectangle in radians
     * @param scale The scale of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(float size, Vector2 position, float angle, Vector2 scale, boolean solid)
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
        transformVertices();
    }
}
