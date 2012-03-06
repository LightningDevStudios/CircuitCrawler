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
        radius = halfSize;
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
        //mass = density * getRadius() * getRadius() * (float)Math.PI;
        mass = 1024;
    }
    
    @Override
    public Vector2[] getSATAxes(Shape s)
    {
        Vector2[] axes = new Vector2[1];
        Vector2[] sVerts = s.getWorldVertices();
        
        axes[0] = Vector2.subtract(sVerts[0], position);
        for (int i = 1; i < sVerts.length; i++)
        {
            Vector2 tempVec = Vector2.subtract(sVerts[i], position);
            if (axes[0].length() > tempVec.length())
            {
                axes[0] = tempVec;
            }
        }
        
        axes[0] = Vector2.normalize(axes[0]);
        return axes;
    }
    
    @Override
    public float[] project(Vector2 axis)
    {
        float[] bounds = new float[2];
        float posDot = Vector2.dot(position, axis);
        
        bounds[0] = posDot - radius;
        bounds[1] = posDot + radius;
        
        return bounds;
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
