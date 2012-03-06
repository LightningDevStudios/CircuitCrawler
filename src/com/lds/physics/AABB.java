package com.lds.physics;

import com.lds.math.Vector2;

/**
 * An Axis-Aligned Bounding Box.
 * @author Lightning Development Studios
 */
public final class AABB 
{
    private final float leftBound;
    private final float rightBound;
    private final float topBound;
    private final float bottomBound;
    
    public AABB(Vector2[] vertices)
    {
        float left = vertices[0].x();
        float right = left;
        float top = vertices[0].y();
        float bottom = top;
        
        for (int i = 1; i < vertices.length; i++)
        {
            Vector2 v = vertices[i];
            if (v.x() < left)
                left = v.x();
            else if (v.x() > right)
                right = v.x();
            if (v.y() > top)
                top = v.y();
            else if (v.y() < bottom)
                bottom = v.y();
        }
        
        leftBound = left;
        rightBound = right;
        topBound = top;
        bottomBound = bottom;
    }
    
    public AABB(float left, float right, float top, float bottom)
    {
        leftBound = left;
        rightBound = right;
        topBound = top;
        bottomBound = bottom;
    }
    
    public AABB toBottomLeftCoords(float worldX, float worldY)
    {
        float halfX = worldX / 2;
        float halfY = worldY / 2;
        
        return new AABB(leftBound + halfX, rightBound + halfX, topBound + halfY, bottomBound + halfY);
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
