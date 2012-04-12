package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.Vector2;

public class GravityPoint extends GlobalForce
{
    private Vector2 point;
    private float g;

    public GravityPoint(Vector2 point, float g)
    {
        this.point = point;
        this.g = g;
    }

    @Override
    public void updateForce(float frameTime)
    {
        for (Shape s : shapes)
        {
            Vector2 v = Vector2.subtract(point, s.getPos());
            float vmag = v.length() + 10;
            float mag = frameTime * g * s.getMass() / vmag / vmag;
            Vector2 i = Vector2.scaleTo(v, mag);
            s.addImpulse(i);
        }
    }
}
