package com.lds.physics;

import com.lds.math.Vector2;

import com.lds.physics.primatives.Shape;

public class RayPoint 
{
    private Shape shape;
    private Vector2 in;
    
    public RayPoint(Shape s, Vector2 i)
    {
        shape = s;
        in = i;
    }
    
    public Shape getShape()
    {
        return shape;
    }
    
    public Vector2 getEntrancePoint()
    {
        return in;
    }
}
