package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primatives.Shape;

public class RaycastData 
{
    private float distance;
    private Shape shape;
    private Vector2 in, out;
    
    public RaycastData(float f, Shape s, Vector2 i, Vector2 o)
    {
        distance = f;
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
