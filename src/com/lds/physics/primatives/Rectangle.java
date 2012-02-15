package com.lds.physics.primatives;

import com.lds.math.Vector2;

public class Rectangle extends Shape 
{
    /****************
     * Constructors *
     ***************/
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, boolean solid)
    {
        this(size, new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, Vector2 position, boolean solid)
    {
        this(size, position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param angle The angle of the rectangle in radians
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, Vector2 position, float angle, boolean solid)
    {
        super(position, angle, solid);
        
        float x = size.x() / 2;
        float y = size.y() / 2;

        float[] vertices = 
        {
            -x,  y,
            -x, -y,
             x, -y,
             x,  y
        };

        this.vertices = vertices;
        transformVertices();
        updateMass();
    }
    
    @Override
    protected void updateMass()
    {
        float width = -vertices[0] * 2;
        float height = vertices[0] * 2;
        mass = density * width * height;
    }
}
