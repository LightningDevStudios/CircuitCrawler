package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;

import java.util.ArrayList;

public abstract class GlobalForce
{
    protected ArrayList<Shape> shapes;

    public GlobalForce()
    {
        shapes = new ArrayList<Shape>();
    }

    public abstract void updateForce(float frameTime);

    /**
     * Adds a shape to the collection of shapes that get affected by this force.
     * @param s The shape to add.
     */
    public void addShape(Shape s)
    {
        this.shapes.add(s);
    }
}
