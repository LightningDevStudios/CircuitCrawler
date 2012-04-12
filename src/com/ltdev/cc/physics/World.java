package com.ltdev.cc.physics;

import android.util.Log;

import com.ltdev.cc.physics.forcegenerators.*;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import java.util.ArrayList;

/**
 * A container for all of the physics objects.
 * @author Lightning Development Studios
 */
public class World 
{
    private Vector2 size;
    
    private boolean paused;

    private CollisionDetector collisionDetector;
    
    private ArrayList<GlobalForce> forces;
    private ArrayList<Shape> shapes;
    //private ArrayList<Contact> contacts;

    /**
     * Initializes a new instance of the World class.
     * @param size The size of the world.
     * @param shapes A list of shapes to initialize the world with.
     */
    public World(Vector2 size, ArrayList<Shape> shapes)
    {
        this.size = size;
        this.shapes = shapes;
        
        forces = new ArrayList<GlobalForce>();
        collisionDetector = new CollisionDetector(this.size, shapes);
    }

    /**
     * Adds an object to the physics world.
     * @param s The object to add.
     */
    public void add(Shape s)
    {
        shapes.add(s);
    }
    
    /**
     * Removes an object from the physics world.
     * @param s The shape to remove.
     */
    public void remove(Shape s)
    {
        shapes.remove(s);
    }
    
    /**
     * Fires a ray through the world and returns when it hits something.
     * @param start The position to start at.
     * @param angle The angle, in radians, to fire the ray at.
     * @return The object the ray hit, as well as other related data.
     */
    public RaycastData raycast(Vector2 start, float angle)
    {
        //TODO make this actually work all the time.
        try
        {
            return collisionDetector.rayCast(start, angle);
        }
        catch (Exception e)
        {
            Log.e("CircuitCrawler", "Raycast failed, unspecified exception.");
        }
        
        return null;
    }
    
    /**
     * Integrates the physics world by a specified dt.
     * @param frameTime The amount of time to integrate for (dt).
     */
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

        //Update the detector and resolve collisions.
        collisionDetector.update(frameTime);

        for (GlobalForce f : forces)
            f.updateForce(frameTime);

        //Apply Forces and Integrate
        for (Shape s : shapes)
        {
            if (s.isStatic())
                continue;
            
            s.integrate(frameTime);
        }
    }
}
