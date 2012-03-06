package com.lds.physics;

import com.lds.physics.primitives.Shape;

import java.util.ArrayList;

public final class GridNode 
{
    private final ArrayList<Shape> shapes;
    
    public GridNode()
    {
        shapes = new ArrayList<Shape>();
    }
    
    public void addShape(Shape s)
    {
        shapes.add(s);
    }
    
    public void removeShape(Shape s)
    {
        shapes.remove(s);
    }
    
    public void clear()
    {
        shapes.clear();
    }
    
    public ArrayList<Shape> shapes()
    {
        return shapes;
    }
    
    public int size()
    {
        return shapes.size();
    }
    
    public Shape get(int i)
    {
        return shapes.get(i);
    }
}
