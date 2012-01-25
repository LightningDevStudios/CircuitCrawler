package com.lds.physics.forcegenerators;

import com.lds.physics.primatives.Shape;

import java.util.ArrayList;

public abstract class GlobalForce
{
    public ArrayList<Shape> shapes;

    public GlobalForce()
    {
        shapes = new ArrayList<Shape>();
    }

    public abstract void UpdateForce(float frameTime);

    public void AddShape(Shape s)
    {
        this.shapes.add(s);
    }
}
