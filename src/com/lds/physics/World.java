package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primatives.*;
import com.lds.physics.forcegenerators.*;

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
            c.Resolve(frameTime);

        for (GlobalForce f : forces)
            f.UpdateForce(frameTime);

        //Apply Forces and Integrate
        for (Shape s : shapes)
        {
            if (s.isStatic)
                continue;
            
            s.integrate(frameTime);
        }
    }
}
