package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;

/**
 * Contains all the data associated with the result of a raycast.
 * @author Lightning Development Studios.
 */
public class RaycastData 
{
    private float distance;
    private Shape shape;
    
    /**
     * Initializes a new instance of the RaycastData class.
     * @param distance The distance the ray traveled.
     * @param s The shape the ray hit.
     */
    public RaycastData(float distance, Shape s)
    {
        this.distance = distance;
        shape = s;
    }
    
    /**
     * Gets the distance of the ray from the start to where it hit an object.
     * @return The distance of the ray.
     */
    public float getDistance() 
    {
        return distance;
    }
    
    /**
     * Gets the shape that the ray collided with.
     * @return The shape that the ray collided with.
     */
    public Shape getShape()
    {
        return shape;
    }
}
