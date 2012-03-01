package com.lds.physics;

import com.lds.math.Vector2;

import com.lds.physics.primatives.Shape;

public class RayPoint 
{
    private Shape shape;
    private Vector2 in, out;

    public RayPoint(Shape s)
    {
        shape = s;
    }
    
    public void setEntrancePoint(Vector2 i)
    {
        in = i;
    }
    
    public void setExitPoint(Vector2 o)
    {
        out = o;
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
