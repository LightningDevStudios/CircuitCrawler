package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primatives.*;

import java.util.ArrayList;
 
public class CollisionDetector
{
    private Vector2 size;
    private ArrayList<Shape> shapes;
    public SpatialHashbrown withBacon;

    public ArrayList<Contact> contacts;

    public CollisionDetector(Vector2 size, ArrayList<Shape> shapes)
    {
        this.size = size;
        this.shapes = shapes;

        withBacon = new SpatialHashbrown(this.size, this.shapes, 0);
        contacts = new ArrayList<Contact>();
    }

    public void Update()
    {
        withBacon.Fry();
        contacts = updateCollisions();
    }

    public ArrayList<Contact> updateCollisions()
    {
        ArrayList<Contact> broadPhaseContacts = withBacon.OmNomNom();
        ArrayList<Contact> nearPhaseContacts = new ArrayList<Contact>();
    
        for (Contact contact : broadPhaseContacts)
        {
            Contact c = colliding(contact);
            if (c != null)
                nearPhaseContacts.add(c);
        }
    
        return nearPhaseContacts;
    }

    public static Contact colliding(Contact c)
    {
        return Colliding(c.a, c.a);
    }

    public static Contact Colliding(Shape a, Shape b)
    {
        if (a == b)
            return null;

        //TODO check a and b with "instanceof" once and store the Types.
        if (a instanceof Circle && b instanceof Circle)
            return RadiusCheckCircles((Circle)a, (Circle)b);
        else if (!RadiusCheck(a, b))
                return null;
        
        if (a instanceof Rectangle)
        {
            if (b instanceof Circle)
                return SATRectangleCircle((Rectangle)a, (Circle)b);
            else if (b instanceof Rectangle)
                return SATRectangles((Rectangle)a, (Rectangle)b);
        }
        else if (b instanceof Circle && b instanceof Rectangle)
        {
            return SATRectangleCircle((Rectangle)b, (Circle)a);
        }
    
        return null;
    }

    public static boolean RadiusCheck(Shape a, Shape b)
    {
        float diagonalA = Vector2.subtract(a.getPos(), a.getWorldVertices()[0]).length();
        float diagonalB = Vector2.subtract(b.getPos(), b.getWorldVertices()[0]).length();
        return Vector2.subtract(a.getPos(), b.getPos()).length() < (float)(diagonalA + diagonalB);
    }

    public static Contact RadiusCheckCircles(Circle a, Circle b)
    {
        Vector2 contactNormal = Vector2.subtract(a.getPos(), b.getPos());
        float penetration = contactNormal.length();

        if (penetration > a.getRadius() + b.getRadius())
            return null;

        contactNormal = Vector2.scale(contactNormal,  1 / penetration);
        Vector2 contactPoint = Vector2.add(b.getPos(), Vector2.scale(contactNormal, b.getRadius()));

        return new Contact(a, b, penetration, contactNormal, contactPoint);
    }

    public static Contact SATRectangleCircle(Rectangle r, Circle c)
    {
        Vector2[] axes = new Vector2[3];

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

            if (!((max1 > min2 && max1 < max2) || (max2 > min1 || max2 < max1)))
                return null;
        }

        return new Contact(r, c);
    }

    public static Contact SATRectangles(Rectangle a, Rectangle b)
    {
        float penetration = Float.MAX_VALUE;
        Vector2 contactNormal = new Vector2(0, 0);
        Vector2 contactPoint = new Vector2(0, 0);

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
            int minAPoint = 0;
            int maxAPoint = 0;
            for (int j = 1; j < a.getWorldVertices().length; j++)
            {
                float dotProd1 = Vector2.dot(axes[i], a.getWorldVertices()[j]);
                if (dotProd1 > maxA)
                {
                    maxA = dotProd1;
                    maxAPoint = j;
                }
                if (dotProd1 < minA)
                {
                    minA = dotProd1;
                    minAPoint = j;
                }
            }

            float minB = Vector2.dot(axes[i], b.getWorldVertices()[0]);
            float maxB = minB;
            int minBPoint = 0;
            int maxBPoint = 0;
            for (int j = 1; j < b.getWorldVertices().length; j++)
            {
                float dotProd2 = Vector2.dot(axes[i], b.getWorldVertices()[j]);
                if (dotProd2 > maxB)
                {
                    maxB = dotProd2;
                    maxBPoint = j;
                }
                if (dotProd2 < minB)
                {
                    minB = dotProd2;
                    minBPoint = j;
                }
            }

            if (maxA >= minB && maxA <= maxB)
            {
                if (maxA - minB < penetration)
                {
                    penetration = maxA - minB;
                    contactNormal = Vector2.negate(axes[i]);
                    if (i < 2)
                        contactPoint = b.getWorldVertices()[minBPoint];
                    else
                        contactPoint = a.getWorldVertices()[maxAPoint];
                }
            }
            else if (maxB >= minA && maxB <= maxA)
            {
                if (maxB - minA < penetration)
                {
                    penetration = maxB - minA;
                    contactNormal = axes[i];
                    if (i < 2)
                        contactPoint = b.getWorldVertices()[maxBPoint];
                    else
                        contactPoint = a.getWorldVertices()[minAPoint];
                }
            }
            else
                return null;
        }

        return new Contact(a, b, penetration, contactNormal, contactPoint);
    }

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