package com.lds.physics;

import com.lds.math.Vector2;

/**
 * An Axis-Aligned Bounding Box
 * @author Devin Reed
 */
public class AABB 
{
    private float leftBound;
    private float rightBound;
    private float topBound;
    private float bottomBound;
    
    public AABB()
    {
    }
    
    public void generateBounds(Vector2[] vertices)
    {
        leftBound = vertices[0].getX();
        rightBound = vertices[0].getX();
        topBound = vertices[0].getY();
        bottomBound = vertices[0].getY();
        
        for (int i = 1; i < vertices.length; i++)
        {
            Vector2 v = vertices[i];
            if (v.getX() < leftBound)
                leftBound = v.getX();
            else if (v.getX() > rightBound)
                rightBound = v.getX();
            if (v.getY() > topBound)
                topBound = v.getY();
            else if (v.getY() < bottomBound)
                bottomBound = v.getY();
        }
    }
    
    public float getLeftBound()
    {
        return leftBound;
    }
    
    public float getRightBound()
    {
        return rightBound;
    }
    
    public float getTopBound()
    {
        return topBound;
    }
    
    public float getBottomBound()
    {
        return bottomBound;
    }
}
