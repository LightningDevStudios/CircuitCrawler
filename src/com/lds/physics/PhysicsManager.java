package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;

public final class PhysicsManager 
{
    private PhysicsManager()
    {
    }
    
    public static void updatePairs(ArrayList<CollisionPair> pairs)
    {
        for (CollisionPair pair : pairs)
            runCollisionPhysics(pair);
    }
	
	public static void updateShapes(ArrayList<Shape> shapes)
	{
	    for (Shape s : shapes)
        {
            s.push(s.getImpulse());
            s.clearImpulse();
            
            for (Vector2 f : s.getForces())
                s.push(f);
                    
            s.setPos(Vector2.add(s.getPos(), s.getVelocity()));
            
            s.push(Vector2.scale(s.getVelocity(), -s.getMass() * s.getFriction()));
        }
	}
	
	public static void runCollisionPhysics(CollisionPair cp)
	{
	    cp.getShape1().addImpulse(Vector2.scale(cp.getShape2().getVelocity(), cp.getShape2().getMass()));
	    cp.getShape2().addImpulse(Vector2.scale(cp.getShape1().getVelocity(), cp.getShape1().getMass()));
	    
	    if (cp.getShape1() instanceof Rectangle)
	    {
	        if (cp.getShape2() instanceof Rectangle)
	            rectanglesBounce(cp);
	        else if (cp.getShape2() instanceof Circle)
	            rectangleCircleBounce(cp);
	    }
	    else if (cp.getShape1() instanceof Circle)
	    {
	        if (cp.getShape2() instanceof Circle)
	            circlesBounce(cp);
	    }
	}
	
    public static void circlesBounce(CollisionPair cp)
    {
        Vector2 axis = Vector2.getNormal(Vector2.subtract(cp.getShape1().getPos(), cp.getShape2().getPos()));
        cp.getShape1().addImpulse(reflect(cp.getShape1().getVelocity(), axis));
        cp.getShape2().addImpulse(reflect(cp.getShape2().getVelocity(), axis));
    }
    
    public static void rectangleCircleBounce(CollisionPair cp)
    {
       Vector2 circleCenter = cp.getShape2().getPos();
       int closestIndex = 0;
       int secondClosestIndex = 0;
       Vector2[] rectVerts = cp.getShape1().getWorldVertices();
       for (int i = 1; i < rectVerts.length; i++)
       {
           if (Vector2.subtract(circleCenter, rectVerts[i]).length() < Vector2.subtract(circleCenter, rectVerts[closestIndex]).length())
               closestIndex = i;
           else if (Vector2.subtract(circleCenter, rectVerts[i]).length() < Vector2.subtract(circleCenter, rectVerts[secondClosestIndex]).length())
               secondClosestIndex = i;
       }
       Vector2 axis = Vector2.getNormal(Vector2.subtract(rectVerts[closestIndex], rectVerts[secondClosestIndex]));
       cp.getShape1().addImpulse(reflect(cp.getShape1().getVelocity(), axis));
       cp.getShape2().addImpulse(reflect(cp.getShape2().getVelocity(), axis));
    }

    public static void rectanglesBounce(CollisionPair cp) 
    {
        Vector2 axis = Vector2.getNormal(Vector2.normalize(cp.getNearestOut()));
        cp.getShape1().addImpulse(reflect(cp.getShape1().getVelocity(), axis));
        cp.getShape2().addImpulse(reflect(cp.getShape2().getVelocity(), axis));
    }
    
    public static Vector2 reflect(Vector2 v, Vector2 axis)
    {
        axis = Vector2.normalize(axis);
        Vector2 projAxisV = Vector2.scale(axis, Vector2.dot(v, axis));
        Vector2 projNormalV = Vector2.subtract(v, projAxisV);
        return Vector2.subtract(projAxisV, projNormalV);
    }
}
