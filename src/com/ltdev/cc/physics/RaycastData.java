package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;

public class RaycastData 
{
    private float distance;
    private Shape shape;
    
    public RaycastData(float distance, Shape s)
    {
        this.distance = distance;
        shape = s;
    }
    
    public float getDistance() 
    {
        return distance;
    }
    
    public Shape getShape()
    {
        return shape;
    }
}
