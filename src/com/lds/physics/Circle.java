package com.lds.physics;

import com.lds.math.Vector2;

public class Circle extends Shape 
{
    /****************
     * Constructors *
     ***************/
    
    /**
     * Initializes a new instance of the Circle class
     */
    public Circle()
    {
        this(1);
    }
    
    /**
     * Initializes a new instance of the Circle class
     * @param size The diameter of the circle
     */
    public Circle(float size)
    {
        this(size, new Vector2(0, 0));
    }
    
    /**
     * Initializes a new instance of the Circle class
     * @param size The diameter of the circle
     * @param position The position of the circle
     */
    public Circle(float size, Vector2 position)
    {
        this(size, position, 0);
    }
    
    /**
     * Initializes a new instance of the Shape class
     * @param size The diameter of the circle
     * @param position The position of the circle
     * @param angle The angle of the circle in radians
     */
    public Circle(float size, Vector2 position, float angle)
    {
        this(size, position, angle, 1);
    }
    
    /**
     * Initializes a new instance of the Circle class
     * @param size The diameter of the circle
     * @param position The position of the circle
     * @param angle The angle of the circle in radians
     * @param scale The scale of the circle
     */
    public Circle(float size, Vector2 position, float angle, float scale)
    {
        super(position, angle, new Vector2(scale));
        
        float halfSize = size / 2;
        float[] vertices = 
            {
                -halfSize, halfSize,     //top left
                -halfSize, -halfSize,    //bottom left
                halfSize, halfSize,    //top right
                halfSize, -halfSize    //bottom right
            };
        this.vertices = vertices;
    }
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    @Override
    public void setScale(Vector2 scale)
    {
        if (scale.getX() == scale.getY())
            super.setScale(scale);
    }
    
    public float getRadius()
    {
        return vertices[5] * scale.getX();
    }
}
