package com.lds.physics;

import com.lds.math.Vector2;

/**
 * An Axis-Aligned Bounding Box.
 * @author Lightning Development Studios
 */
public class AABB 
{
    private float leftBound;
    private float rightBound;
    private float topBound;
    private float bottomBound;
    
    public AABB(Vector2[] vertices)
    {
        generateBounds(vertices);
    }
    
    public AABB(float left, float right, float top, float bottom)
    {
        leftBound = left;
        rightBound = right;
        topBound = top;
        bottomBound = bottom;
    }
    
    public void convertToBottomLeftCoords(float worldX, float worldY)
    {
        leftBound += worldX / 2;
        rightBound += worldX / 2;
        topBound += worldY / 2;
        bottomBound += worldY / 2;
    }
    
    public void generateBounds(Vector2[] vertices)
    {
        leftBound = vertices[0].x();
        rightBound = vertices[0].x();
        topBound = vertices[0].y();
        bottomBound = vertices[0].y();
        
        for (int i = 1; i < vertices.length; i++)
        {
            Vector2 v = vertices[i];
            if (v.x() < leftBound)
                leftBound = v.x();
            else if (v.x() > rightBound)
                rightBound = v.x();
            if (v.y() > topBound)
                topBound = v.y();
            else if (v.y() < bottomBound)
                bottomBound = v.y();
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
