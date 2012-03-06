package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;

import java.util.ArrayList;

public abstract class GlobalForce
{
    public ArrayList<Shape> shapes;

    public GlobalForce()
    {
        shapes = new ArrayList<Shape>();
    }

    public abstract void UpdateForce(float frameTime);

    public void addShape(Shape s)
    {
        this.shapes.add(s);
    }
}
