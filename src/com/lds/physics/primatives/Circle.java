package com.lds.physics.primatives;

import com.lds.math.Vector2;

/**
 * A Shape that represents a circle.
 * @author Lightning Development Studios
 */
public class Circle extends Shape
{
    public float radius;
    
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
        super(position, angle, solid);
        
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
        updateMass();
    }
    
    @Override
    protected void updateMass()
    {
        mass = density * getRadius() * getRadius() * (float)Math.PI;
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
        return vertices[0];
    }
}
