package com.ltdev.physics;

import com.ltdev.math.Vector2;

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
    
    /**
     * Initializes a new instance of the AABB class.
     * @param vertices An array of vertices to calculate the bounding box for.
     */
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
    
    /**
     * Initializes a new instance of the AABB class.
     * @param left The left bound.
     * @param right The right bound.
     * @param top The top bound.
     * @param bottom The bottom bound.
     */
    public AABB(float left, float right, float top, float bottom)
    {
        leftBound = left;
        rightBound = right;
        topBound = top;
        bottomBound = bottom;
    }
    
    /**
     * Converts an AABB from world-centered-at-(0,0) coordinates to bottom-left-at-(0,0) coordinates.
     * @param worldX The size of the world in the X direction.
     * @param worldY The size of the world in the Y direction.
     * @return Another AABB in the new coordinate system.
     */
    public AABB toBottomLeftCoords(float worldX, float worldY)
    {
        float halfX = worldX / 2;
        float halfY = worldY / 2;
        
        return new AABB(leftBound + halfX, rightBound + halfX, topBound + halfY, bottomBound + halfY);
    }
    
    /**
     * Gets the left bound.
     * @return The left bound.
     */
    public float getLeftBound()
    {
        return leftBound;
    }
    
    /**
     * Gets the right bound.
     * @return The right bound.
     */
    public float getRightBound()
    {
        return rightBound;
    }
    
    /**
     * Gets the top bound.
     * @return The top bound.
     */
    public float getTopBound()
    {
        return topBound;
    }
    
    /**
     * Gets the bottom bound.
     * @return The bottom bound.
     */
    public float getBottomBound()
    {
        return bottomBound;
    }
}
