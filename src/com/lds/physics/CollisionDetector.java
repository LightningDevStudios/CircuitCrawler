package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primitives.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
 
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

    public RaycastData rayCast(Vector2 start, float angle)
    {
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        Vector2 dir = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
        float slope = dir.y() / dir.x();
        float invSlope = - dir.x() / dir.y();
        float yIntersect = dir.y() - slope * dir.x();
        float xIntersect = dir.x() - invSlope * dir.y();
        
        boolean xIsMax = dir.x() < 0;
        boolean yIsMax = dir.y() < 0;
        
        for (int i = 0; i <= spatialHash.cellsX; i++)
        {
            float xValue = i * spatialHash.cellSizeX;
            
            if (xIsMax)
            {
                if (xValue < start.x())
                    continue;
            }
            else
            {
                if (xValue > start.x())
                    continue;
            }
            
            float yValue = slope * xValue + yIntersect;
            int yIndex = (int)(yValue / spatialHash.cellSizeY);
            ArrayList<Shape> hashShapes;
            if (i > 0)
            {
                hashShapes = spatialHash.getBucketShapes(i - 1, yIndex);
                for (Shape s : hashShapes)
                {
                    if (!shapes.contains(s))
                        shapes.add(s);
                }
                
            }
            
            hashShapes = spatialHash.getBucketShapes(i, yIndex);
            for (Shape s : hashShapes)
            {
                if (!shapes.contains(s))
                    shapes.add(s);
            }
        }       
        
        for (int i = 0; i <= spatialHash.cellsY; i++)
        {
            float yValue = i * spatialHash.cellSizeY;
            
            if (yIsMax)
            {
                if (yValue < start.y())
                    continue;
            }
            else
            {
                if (yValue > start.y())
                    continue;
            }
            
            float xValue = invSlope * yValue + xIntersect;
            int xIndex = (int)(xValue / spatialHash.cellSizeX);
            ArrayList<Shape> hashShapes;
            if (i > 0)
            {
                hashShapes = spatialHash.getBucketShapes(i - 1, xIndex);
                for (Shape s : hashShapes)
                {
                    if (!shapes.contains(s))
                        shapes.add(s);
                }
                
            }
            
            hashShapes = spatialHash.getBucketShapes(i, xIndex);
            for (Shape s : hashShapes)
            {
                if (!shapes.contains(s))
                    shapes.add(s);
            }
        }       
        
        TreeMap<Float, Shape> distanceHash = new TreeMap<Float, Shape>();
        for(int i = 0; i < shapes.size(); i++)
            distanceHash.put(Vector2.subtract(start, shapes.get(i).getPos()).length(), shapes.get(i));       
        
        for(int i = 0; i < distanceHash.size(); i++)
        {
            Shape s = distanceHash.get(i);
            if (s instanceof Rectangle)
            {
                Vector2 axis = Vector2.perpendicularLeft(dir);
                Vector2[] verts = s.getWorldVertices();
                float startProj = Vector2.dot(start, axis);
                boolean intersecting = false;
                boolean toTheLeft = false;
                boolean toTheRight = false;
                for (Vector2 vert : verts)
                {
                    if (Vector2.dot(vert, axis) < startProj)
                    {
                        if (toTheRight)
                        {
                            intersecting = true;
                            break;
                        }
                        else
                            toTheLeft = true;
                    }
                    if (Vector2.dot(vert, axis) > startProj)
                    {
                        if (toTheLeft)
                        {
                            intersecting = true;
                            break;
                        }
                        else
                            toTheRight = true;
                    }
                }
                
                if (intersecting)
                {
                    Vector2 intersect1 = null;
                    Vector2 intersect2 = null;
                    intersect1 = PointOfIntersection(start, Vector2.scale(dir, 10000), verts[4], verts[0]);
                    for (int j = 0; j < 3; j++)
                    {
                        Vector2 tempIntersect = PointOfIntersection(start, Vector2.scale(dir, 10000), verts[j], verts[j + 1]);
                        if (tempIntersect != null)
                        {
                            if (intersect1 == null)
                                intersect1 = tempIntersect;
                            else
                            {
                                intersect2 = tempIntersect;
                                break;
                            }
                        }
                    }
                    
                    float distance1 = Vector2.subtract(intersect1, start).length();
                    float distance2 = Vector2.subtract(intersect2, start).length();
                    if (distance1 < distance2)
                        return new RaycastData(distance1, s, intersect1, intersect2);
                    else
                        return new RaycastData(distance2, s, intersect2, intersect1);
                }
            }
        }
        
        return null;
    }
    
    public Vector2 PointOfIntersection(Vector2 line1Start, Vector2 line1End, Vector2 line2Start, Vector2 line2End)
    {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = line1End.x() - line1Start.x();     
        s1_y = line1End.y() - line1Start.y();
        s2_x = line2End.x() - line2Start.x();     
        s2_y = line2End.y() - line2Start.y();

        float s, t;
        s = (-s1_y * (line1Start.x() - line2Start.x()) + s1_x * (line1Start.y() - line2Start.y())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (line1Start.y() - line2Start.y()) - s2_y * (line1Start.x() - line2Start.x())) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            return new Vector2( line1Start.x() + (t * s1_x), line1Start.y() + (t * s1_y));
        }
        return null;
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
