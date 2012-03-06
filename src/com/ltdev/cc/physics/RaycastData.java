package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.Vector2;

public class RaycastData 
{
    private float distance;
    private Shape shape;
    private Vector2 in, out;
    
    public RaycastData(float distance, Shape s, Vector2 i, Vector2 o)
    {
        this.distance = distance;
        shape = s;
        in = i;
        out = o;
    }
    
    public float getDistance() 
    {
        return distance;
    }
    
    public Shape getShape()
    {
        return shape;
    }
    
    public Vector2 getEntrancePoint()
    {
        return in;
    }
    
    public Vector2 getExitPoint()
    {
        return out;
    }
}
