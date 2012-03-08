package com.ltdev.cc.physics;

import com.ltdev.cc.physics.forcegenerators.*;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import java.util.ArrayList;

public class World 
{
    public Vector2 size;
    
    public boolean paused;

    public CollisionDetector collisionDetector;
    
    private ArrayList<GlobalForce> forces;
    private ArrayList<Shape> shapes;
    private ArrayList<Contact> contacts;

    public World(Vector2 size, ArrayList<Shape> shapes)
    {
        this.size = size;
        this.shapes = shapes;
        
        forces = new ArrayList<GlobalForce>();
        collisionDetector = new CollisionDetector(this.size, shapes);
    }

    public void add(Shape s)
    {
        shapes.add(s);
    }
    
    public void remove(Shape s)
    {
        shapes.remove(s);
    }
    
    public RaycastData raycast(Vector2 start, float angle)
    {
        //TODO make this actually work all the time.
        try
        {
            return collisionDetector.rayCast(start, angle);
        }
        catch (Exception e)
        { }
        
        return null;
    }
    
    public void integrate(float frameTime)
    {
        frameTime /= 1000;
        
        if (paused)
            return;
        
        if (frameTime > 1)
            return;

        //Check for shapes
        if (shapes.size() < 1)
            return;

        //Update the detector
        collisionDetector.update();

        for (Contact c : collisionDetector.contacts)
            c.resolve(frameTime);

        for (GlobalForce f : forces)
            f.UpdateForce(frameTime);

        //Apply Forces and Integrate
        for (Shape s : shapes)
        {
            if (s.isStatic())
                continue;
            
            s.integrate(frameTime);
        }
    }
}
