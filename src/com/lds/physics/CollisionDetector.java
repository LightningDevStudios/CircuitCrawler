package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primatives.*;

import java.util.ArrayList;
 
public class CollisionDetector
{
    private Vector2 size;
    private ArrayList<Shape> shapes;
    public SpatialHashGrid spatialHash;

    public ArrayList<Contact> contacts;
    
    public CollisionDetector(Vector2 size, ArrayList<Shape> shapes)
    {
        this.size = size;
        this.shapes = shapes;

        spatialHash = new SpatialHashGrid(shapes, size.x(), size.y(), 2, 2);
        contacts = new ArrayList<Contact>();
    }

    public void update()
    {
        spatialHash.populate();
        updateCollisions();
    }

    public void updateCollisions()
    {        
        spatialHash.getCollisionPairs(contacts);
        
        for (int i = contacts.size() - 1; i >= 0; i--)
        {
            if (!colliding(contacts.get(i)))
                contacts.remove(i);
        }
    }

    public static boolean colliding(Contact c)
    {        
        Shape a = c.a;
        Shape b = c.b;
        
        if (a instanceof Circle || b instanceof Circle)
            System.out.println("lol");
        
        if (a == b)
            return false;

        //TODO check a and b with "instanceof" once and store the Types.
        if (a instanceof Circle && b instanceof Circle)
            return RadiusCheckCircles(c);
        else if (!RadiusCheck(a, b))
                return false;
        
        /*if (a instanceof Rectangle)
        {
            if (b instanceof Circle)
                return SATRectangleCircle(c, true);
            else if (b instanceof Rectangle)
                return SATRectangles(c);
        }
        else if (b instanceof Circle && b instanceof Rectangle)
        {
            return SATRectangleCircle(c, false);
        }*/
        return SAT(c);
    
        //return false;
    }

    public static boolean RadiusCheck(Shape a, Shape b)
    {
        float diagonalA = Vector2.subtract(a.getPos(), a.getWorldVertices()[0]).length();
        float diagonalB = Vector2.subtract(b.getPos(), b.getWorldVertices()[0]).length();
        return Vector2.subtract(a.getPos(), b.getPos()).length() < (float)(diagonalA + diagonalB);
    }

    public static boolean RadiusCheckCircles(Contact c)
    {
        Circle a = (Circle)c.a;
        Circle b = (Circle)c.b;
        
        Vector2 contactNormal = Vector2.subtract(a.getPos(), b.getPos());
        float penetration = contactNormal.length();

        if (penetration > a.getRadius() + b.getRadius())
            return false;

        contactNormal = Vector2.scale(contactNormal,  1 / penetration);

        c.penetration = penetration;
        c.contactNormal = contactNormal;
        
        return true;
    }
    
    public static boolean SAT(Contact c)
    {   
        float a;
        if (c.a instanceof Circle || c.b instanceof Circle)
            a = 4;
        
        float penetration = Float.MAX_VALUE;
        Vector2 contactNormal = null;
        
        Vector2[] axesA = c.a.getSATAxes(c.b);
        int lengthA = axesA.length;
        Vector2[] axesB = c.b.getSATAxes(c.a);
        int lengthB = axesB.length;
        Vector2[] axes = new Vector2[lengthA +  lengthB];
        
        for (int i = 0; i < lengthA; i++)
            axes[i] = axesA[i];
        
        for (int i = 0; i < lengthB; i++)
            axes[i + lengthA] = axesB[i];
        
        for (int i = 0; i < axes.length; i++)
        {
            float[] projA = c.a.project(axes[i]);
            float[] projB = c.b.project(axes[i]);
            
            if (projA[1] >= projB[0] && projA[1] <= projB[1])
            {
                if (projA[1] - projB[0] < penetration)
                {
                    penetration = projA[1] - projB[0];
                    contactNormal = Vector2.negate(axes[i]);
                }
            }
            else if (projB[1] >= projA[0] && projB[1] <= projA[1])
            {
                if (projB[1] - projA[0] < penetration)
                {
                    penetration = projB[1] - projA[0];
                    contactNormal = axes[i];
                }
            }
            else
                return false;
        }
        
        c.penetration = penetration;
        c.contactNormal = contactNormal;
        
        return true;
    }

   /* public static boolean SATRectangles(Contact c)
    {
        Rectangle a = (Rectangle)c.a;
        Rectangle b = (Rectangle)c.b;
        
        float penetration = Float.MAX_VALUE;
        Vector2 contactNormal = null;
        Vector2[] axes = new Vector2[4];

        Vector2 v = Vector2.subtract(a.getWorldVertices()[0], a.getWorldVertices()[1]);
        axes[0] = new Vector2(Math.abs(v.x()), Math.abs(v.y()));
        axes[1] = Vector2.perpendicularLeft(axes[0]);

        v = Vector2.subtract(b.getWorldVertices()[0], b.getWorldVertices()[1]);
        axes[2] = new Vector2(Math.abs(v.x()), Math.abs(v.y()));
        axes[3] = Vector2.perpendicularLeft(axes[2]);

        for (int i = 0; i < axes.length; i++)
            axes[i] = Vector2.normalize(axes[i]);

        int axesCount = 4;
        if (axes[2] == axes[0] || axes[2] == axes[1])
            axesCount = 2;

        for (int i = 0; i < axesCount; i++)
        {
            float minA = Vector2.dot(axes[i], a.getWorldVertices()[0]);
            float maxA = minA;
            for (int j = 1; j < a.getWorldVertices().length; j++)
            {
                float dotProd1 = Vector2.dot(axes[i], a.getWorldVertices()[j]);
                if (dotProd1 > maxA)
                {
                    maxA = dotProd1;
                }
                if (dotProd1 < minA)
                {
                    minA = dotProd1;
                }
            }

            float minB = Vector2.dot(axes[i], b.getWorldVertices()[0]);
            float maxB = minB;
            for (int j = 1; j < b.getWorldVertices().length; j++)
            {
                float dotProd2 = Vector2.dot(axes[i], b.getWorldVertices()[j]);
                if (dotProd2 > maxB)
                {
                    maxB = dotProd2;
                }
                if (dotProd2 < minB)
                {
                    minB = dotProd2;
                }
            }

            if (maxA >= minB && maxA <= maxB)
            {
                if (maxA - minB < penetration)
                {
                    penetration = maxA - minB;
                    contactNormal = Vector2.negate(axes[i]);
                }
            }
            else if (maxB >= minA && maxB <= maxA)
            {
                if (maxB - minA < penetration)
                {
                    penetration = maxB - minA;
                    contactNormal = axes[i];
                }
            }
            else
                return false;
        }

        c.penetration = penetration;
        c.contactNormal = contactNormal;
        
        return true;
    }*/

    public static boolean ContainsPoint(Circle c, Vector2 v)
    {
        return Vector2.subtract(c.getPos(), v).length() < c.getRadius();
    }

    public static boolean ContainsPoint(Rectangle r, Vector2 v)
    {
        Vector2[] verts = r.getWorldVertices();
        Vector2 axis = Vector2.subtract(verts[0], verts[1]);

        for (int i = 0; i < 2; i++)
        {
            float min = Vector2.dot(axis, verts[0]);
            float max = min;
            for (int j = 1; j < verts.length; j++)
            {
                float dotProd = Vector2.dot(axis, verts[j]);
                if (dotProd > max)
                    max = dotProd;
                if (dotProd < min)
                    min = dotProd;
            }

            float projection = Vector2.dot(axis, v);

            if (projection < min || projection > max)
                return false;

            axis = Vector2.normalize(axis);
        }

        return true;
    }
}
