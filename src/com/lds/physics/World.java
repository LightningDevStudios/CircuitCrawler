package com.lds.physics;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;
import com.lds.physics.primatives.*;
import com.lds.physics.forcegenerators.*;

import java.util.ArrayList;

public class World 
{
    public Vector2 size;
    
    public boolean paused;

    public CollisionDetector collisionDetector;
    private ArrayList<Entity> entList;
    public ArrayList<GlobalForce> forces;

    private ArrayList<Shape> shapes;
    public ArrayList<Contact> contacts;

    public World(Vector2 size, ArrayList<Entity> entList)
    {
        this.size = size;
        
        this.entList = entList;
        shapes = new ArrayList<Shape>();
        for (Entity ent : entList)
            shapes.add(ent.getShape());
        
        forces = new ArrayList<GlobalForce>();

        collisionDetector = new CollisionDetector(this.size, shapes);
    }

    /*public void remove(Entity... ents)
    {
        for (Entity ent : ents)
        {
            entList.remove(ent);
            shapes.remove(ent.getShape());
        }
    }*/

    public void integrate(float frameTime)
    {
        frameTime /= 1000;
        
        if (paused)
            return;

        //if (frameTime >= 0.05f)
           // return;

        //Check for shapes
        if (shapes.size() < 1)
            return;

        //Update the detector
        collisionDetector.update();

        //resolve collisions
        if (collisionDetector.contacts.size() >= 1)
        {
            for (Contact c : collisionDetector.contacts)
                c.Resolve(frameTime);
        }

        for (GlobalForce f : forces)
            f.UpdateForce(frameTime);

        //Apply Forces and Integrate
        for (Shape s : shapes)
        {
            if (s.isStatic)
                continue;
            
            //TODO: add friction
            //s.addImpulse(new Vector2(0, -9.81f * s.getMass() * frameTime));
            s.Integrate(frameTime);
        }

        //Update entities
        for (Entity ent : entList)
        {
            //if (ent != null)
                //ent.update();
        }
    }
}
