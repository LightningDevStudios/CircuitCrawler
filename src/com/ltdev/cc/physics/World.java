/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
