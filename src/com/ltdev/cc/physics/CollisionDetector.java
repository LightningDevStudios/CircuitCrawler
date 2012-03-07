package com.ltdev.cc.physics;

import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.Player;
import com.ltdev.cc.entity.LaserShooter;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import java.util.Collection;
import java.util.ArrayList;
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
        /***************
         * Broad Phase *
         ***************/
        
        start = Vector2.add(start, new Vector2(spatialHash.cellsX * spatialHash.cellSizeX / 2, spatialHash.cellsY * spatialHash.cellSizeY / 2));
        
        ArrayList<Shape> lineShapes = new ArrayList<Shape>();
        Vector2 dir = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
        float slope = dir.y() / dir.x();
        float invSlope = dir.x() / dir.y();
        float yIntersect = start.y() - slope * start.x();
        float xIntersect = start.x() - invSlope * start.y();
        
        boolean xIsMax = dir.x() < 0;
        boolean yIsMax = dir.y() < 0;
        
        ArrayList<Integer> xIgnoreValues = new ArrayList<Integer>();
        ArrayList<Integer> yIgnoreValues = new ArrayList<Integer>();
        
        int startXIndex = (int)(start.x() / spatialHash.cellSizeX);
        int startYIndex = (int)(start.y() / spatialHash.cellSizeY);
        
        if (xIsMax)
        {
            for (int i = startXIndex + 1; i <= spatialHash.cellsX; i++)
                xIgnoreValues.add(i);
        }
        else
        {           
            for (int i = 0; i <= startXIndex; i++)
                xIgnoreValues.add(i);
        }
        
        if (yIsMax)
        {
            for (int i = startYIndex + 1; i <= spatialHash.cellsY; i++)
                yIgnoreValues.add(i);
        }
        else
        {            
            for (int i = 0; i <= startYIndex; i++)
                yIgnoreValues.add(i);
        }
        
        for (int i = 0; i <= spatialHash.cellsX; i++)
        {
            if (xIgnoreValues.contains(i))
                continue;
            
            float xValue = i * spatialHash.cellSizeX;
            float yValue = slope * xValue + yIntersect;
            int yIndex = (int)(yValue / spatialHash.cellSizeY);
            if (yIndex >= spatialHash.cellsY || (i != 0 && i < spatialHash.cellsX - 1 && xIgnoreValues.contains(i + 1)))
                continue;
            
            ArrayList<Shape> hashShapes;
            if (i > 0)
            {
                hashShapes = spatialHash.getBucketShapes(i - 1, yIndex);
                if (hashShapes != null)
                {
                    for (Shape s : hashShapes)
                    {
                        if (!lineShapes.contains(s))
                            lineShapes.add(s);
                    }
                }
                
            }
            
            if (i < spatialHash.cellsX)
            {
                hashShapes = spatialHash.getBucketShapes(i, yIndex);
                if (hashShapes != null)
                {
                    for (Shape s : hashShapes)
                    {
                        if (!lineShapes.contains(s))
                            lineShapes.add(s);
                    }
                }
            }
        }       
        
        for (int i = 0; i <= spatialHash.cellsY; i++)
        {
            if (yIgnoreValues.contains(i))
                continue;
            
            float yValue = i * spatialHash.cellSizeY;   
            float xValue = invSlope * yValue + xIntersect;
            int xIndex = (int)(xValue / spatialHash.cellSizeX);
            if (xIndex >= spatialHash.cellsX || (i != 0 && i < spatialHash.cellsY - 1 && yIgnoreValues.contains(i + 1)))
                continue;
            
            ArrayList<Shape> hashShapes;
            if (i > 0)
            {
                hashShapes = spatialHash.getBucketShapes(xIndex, i - 1);
                if (hashShapes != null)
                {
                    for (Shape s : hashShapes)
                    {
                        if (!lineShapes.contains(s))
                            lineShapes.add(s);
                    }
                }
            }
            
            if (i < spatialHash.cellsY)
            {
                hashShapes = spatialHash.getBucketShapes(xIndex, i);
                if (hashShapes != null)
                {
                    for (Shape s : hashShapes)
                    {
                        if (!lineShapes.contains(s))
                            lineShapes.add(s);
                    }
                }
            }
        }       
        
        /**************
         * Near Phase *
         **************/
        
        start = Vector2.subtract(start, new Vector2(spatialHash.cellsX * spatialHash.cellSizeX / 2, spatialHash.cellsY * spatialHash.cellSizeY / 2));
        
        for (int i = lineShapes.size() - 1; i >= 0; i--)
        {
            if (!lineShapes.get(i).isSolid() || (lineShapes.get(i).getInteractListener() != null && lineShapes.get(i).getInteractListener().getEntity() instanceof Player))
            {
                lineShapes.remove(i);
            }
        }
        
        TreeMap<Float, Shape> distanceHash = new TreeMap<Float, Shape>();
        
        for (int i = 0; i < lineShapes.size(); i++)
            distanceHash.put(Vector2.subtract(start, lineShapes.get(i).getPos()).length(), lineShapes.get(i));
        
        Object[] shapeList = distanceHash.values().toArray();
        
        for (int i = 0; i < shapeList.length; i++)
        {
            Shape s = (Shape)shapeList[i];

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
                if (s instanceof Rectangle)
                {
                    Vector2 intersect1 = null;
                    Vector2 intersect2 = null;
                    intersect1 = PointOfIntersection(start, Vector2.scale(dir, 10000), verts[3], verts[0]);
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
                    
                    if (intersect1 == null || intersect2 == null)
                        continue;
                    
                    float distance1 = Vector2.subtract(intersect1, start).length();
                    float distance2 = Vector2.subtract(intersect2, start).length();
                    if (distance1 < distance2)
                        return new RaycastData(distance1, s);
                    else
                        return new RaycastData(distance2, s);
                }
                else if (s instanceof Circle)
                {
                    Vector2 toCircle = Vector2.subtract(s.getPos(), start);
                    Vector2 scaledDir = Vector2.scale(dir, toCircle.length());
                    if (Vector2.subtract(toCircle, scaledDir).length() < ((Circle)s).getRadius())
                        return new RaycastData(toCircle.length(), s);
                    else
                        continue;
                }
            }
            else
                continue;
            
        }
        
        return null;
    }
    
    public Vector2 PointOfIntersection(Vector2 line1Start, Vector2 line1End, Vector2 line2Start, Vector2 line2End)
    {
        float s1x, s1y, s2x, s2y;
        s1x = line1End.x() - line1Start.x();     
        s1y = line1End.y() - line1Start.y();
        s2x = line2End.x() - line2Start.x();     
        s2y = line2End.y() - line2Start.y();

        float s, t;
        s = (-s1y * (line1Start.x() - line2Start.x()) + s1x * (line1Start.y() - line2Start.y())) / (-s2x * s1y + s1x * s2y);
        t = (s2x * (line1Start.y() - line2Start.y()) - s2y * (line1Start.x() - line2Start.x())) / (-s2x * s1y + s1x * s2y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            return new Vector2(line1Start.x() + (t * s1x), line1Start.y() + (t * s1y));
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
        float penetration = a.getRadius() + b.getRadius() - contactNormal.length();

        if (penetration < 0)
            return false;

        contactNormal = Vector2.normalize(contactNormal);

        c.penetration = penetration;
        c.contactNormal = contactNormal;
        
        return true;
    }
    
    public static boolean SAT(Contact c)
    {      
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

    public static boolean ContainsPoint(Shape shape, Vector2 v)
    {
        if (shape instanceof Circle)
            return Vector2.subtract(shape.getPos(), v).length() < ((Circle)shape).getRadius(); 
        else if (shape instanceof Rectangle)
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
