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

    public ArrayList<RayPoint> rayCast(Vector2 start, Vector2 end)
    {
        //TODO check quad tree for grids before doing this
        ArrayList<RayPoint> points = new ArrayList<RayPoint>();
        
        for(int i = 1; i < Vector2.subtract(start, end).length(); i++)
        {
            Vector2 lineMag = Vector2.subtract(start, end);
            Vector2 line = Vector2.normalize(lineMag);
            
            Vector2 point = Vector2.add(start, new Vector2((lineMag.length() + (float)i) * line.x(), (lineMag.length() + (float)i) * line.y()));
            
            for(int j = 0; j < shapes.size(); j++)
            {
                if(ContainsPoint(shapes.get(j), point))
                {
                    boolean contains = false;
                    for(int k = 0; k < points.size(); k++)
                    {
                        if(points.get(k).getShape() == shapes.get(j))
                            contains = true;
                    }
                    
                    if(!contains)
                    {
                        points.add(new RayPoint(shapes.get(j), point));
                    }
                }
            }
        }
        return points;
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
        
        if (a == b)
            return false;

        //TODO check a and b with "instanceof" once and store the Types.
        if (a instanceof Circle && b instanceof Circle)
            return RadiusCheckCircles(c);
        else if (!RadiusCheck(a, b))
                return false;
        
        if (a instanceof Rectangle)
        {
            if (b instanceof Circle)
                return SATRectangleCircle(c, true);
            else if (b instanceof Rectangle)
                return SATRectangles(c);
        }
        else if (b instanceof Circle && b instanceof Rectangle)
        {
            return SATRectangleCircle(c, false);
        }
    
        return false;
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
        Vector2 contactPoint = Vector2.add(b.getPos(), Vector2.scale(contactNormal, b.getRadius()));

        c.penetration = penetration;
        c.contactNormal = contactNormal;
        
        return true;
    }

    public static boolean SATRectangleCircle(Contact contact, boolean aIsRect)
    {
        Rectangle r;
        Circle c;
        
        if (aIsRect)
        {
            r = (Rectangle)contact.a;
            c = (Circle)contact.b;
        }
        
        else
        {
            r = (Rectangle)contact.b;
            c = (Circle)contact.a;
        }
        
        Vector2[] axes = new Vector2[3];
        float penetration = Float.MAX_VALUE;
        Vector2 contactNormal = null;

        Vector2 v = Vector2.subtract(r.getWorldVertices()[0], r.getWorldVertices()[1]);
        axes[0] = new Vector2(Math.abs(v.x()), Math.abs(v.y()));
        axes[1] = Vector2.perpendicularLeft(axes[0]);
        axes[2] = Vector2.subtract(r.getWorldVertices()[0], c.getPos());
        for (int i = 1; i < r.getWorldVertices().length; i++)
        {
            Vector2 tempVec = Vector2.subtract(r.getWorldVertices()[i], c.getPos());
            if (axes[2].length() > tempVec.length())
            {
                axes[2] = tempVec;
            }
        }

        for(int i = 0; i < axes.length; i++)
        {
            axes[i] = Vector2.normalize(axes[i]);

            float min1 = Vector2.dot(axes[i], r.getWorldVertices()[0]);
            float max1 = min1;
            for (int j = 1; j < r.getWorldVertices().length; j++)
            {
                float dotProd1 = Vector2.dot(axes[i], r.getWorldVertices()[j]);
                if (dotProd1 > max1)
                    max1 = dotProd1;
                if (dotProd1 < min1)
                    min1 = dotProd1;
            }

            float min2 = Vector2.dot(axes[i], Vector2.add(c.getPos(), new Vector2(c.getRadius() * axes[i].x(), c.getRadius() * axes[i].y())));
            float max2 = Vector2.dot(axes[i], Vector2.subtract(c.getPos(), new Vector2(c.getRadius() * axes[i].x(), c.getRadius() * axes[i].y())));
            if (min2 > max2)
            {
                float temp = min2;
                min2 = max2;
                max2 = temp;
            }

            if (max1 >= min2 && max1 <= max2)
            {
                if (max1 - min2 < penetration)
                {
                    penetration = max1 - min2;
                    contactNormal = Vector2.negate(axes[i]);
                }
            }
            else if (max2 >= min1 && max2 <= max1)
            {
                if (max2 - min1 < penetration)
                {
                    penetration = max2 - min1;
                    contactNormal = axes[i];
                }
            }
            else
                return false;
        }

        contact.penetration = penetration;
        contact.contactNormal = contactNormal;
        
        return true;
    }

    public static boolean SATRectangles(Contact c)
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
    }

    public static boolean ContainsPoint(Shape shape, Vector2 v)
    {
        if(shape instanceof Circle)
            return Vector2.subtract(shape.getPos(), v).length() < ((Circle)shape).getRadius();
        
        else if(shape instanceof Rectangle)
        {
            Vector2[] verts = shape.getWorldVertices();
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
        return false;
    }
}