package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.*;
import com.ltdev.physics.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SpatialHashGrid
{
    private float sceneWidth, sceneHeight;
    public int cellsX, cellsY;
    public float cellSizeX, cellSizeY;

    private ArrayList<Shape> elements;
    private HashMap<Integer, ArrayList<Shape>> buckets;

    public SpatialHashGrid(ArrayList<Shape> elements, float sceneWidth, float sceneHeight, int cellsX, int cellsY)
    {
        this.sceneHeight = sceneHeight;
        this.sceneWidth = sceneWidth;
        this.cellsX = cellsX;
        this.cellsY = cellsY;

        this.elements = elements;
        
        cellSizeX = sceneWidth / (float)cellsX;
        cellSizeY = sceneHeight / (float)cellsY;

        buckets = new HashMap<Integer, ArrayList<Shape>>();

        for (int i = 0; i < cellsX * cellsY; i++)
            buckets.put(i, new ArrayList<Shape>());
    }

    public void AddRange(ArrayList<Shape> elements)
    {
        this.elements.addAll(elements);
    }

    /**
     * Clears and re-creates the grid. Faster than updating all existing items.
     */
    public void populate()
    {
        clear();

        for (Shape item : elements)
        {

            for (int i : GetBuckets(new AABB(item.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight)))
            {
                buckets.get(i).add(item);
            }
        }
    }

    /**
     * Gets every possible pair of neighbors.
     * @param neighbors An ArrayList of Contacts to store the pairs in.
     */
    public void getCollisionPairs(ArrayList<Contact> neighbors)
    {
        neighbors.clear();

        //iterate through all the buckets
        for (ArrayList<Shape> bucket : buckets.values())
        {
            int itemCount = bucket.size();
            if (itemCount < 2)
                continue;
            
            for (int i = 0; i < itemCount; i++)
                for (int j = i + 1; j < itemCount; j++)
                    neighbors.add(new Contact(bucket.get(i), bucket.get(j)));
        }
    }

    /**
     * Gets all the items that are near a specified item.
     * @param item An item contained in the grid.
     * @return All of the items near item.
     */
    public ArrayList<Shape> GetNeighbors(Shape item)
    {
        ArrayList<Shape> neighbors = new ArrayList<Shape>();

        if (!elements.contains(item))
            return null;
        
        for (int i : GetBuckets(new AABB(item.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight)))
        {
            neighbors.addAll(buckets.get(i));
            neighbors.remove(item);
        }

        return neighbors;
    }

    /**
     * Gets all the items located in the box centered at a point with a specified size.
     * @param point The center of the area to check.
     * @param size The size of the area to check.
     * @return An ArrayList of items contained roughly in the specified area.
     */
    public ArrayList<Shape> GetContainedItems(Vector2 point, Vector2 size)
    {
        float x = point.x();
        float y = point.y();

        float halfX = sceneWidth / 2;
        float halfY = sceneHeight / 2;
        
        float left = x - size.x() / 2 + halfX;
        float right = x + size.x() / 2 + halfX;
        float top = y + size.y() / 2 + halfY;
        float bottom = y - size.y() / 2 + halfY;
        
        return GetContainedItems(new AABB(left, right, top, bottom));
    }

    /**
     * Gets all the items located in a bounding box.
     * 
     * This method assumes that item is not contained in the grid, unlike GetNeighbors.
     * Assuming box is of the type Shape, this method and GetNeighbors will return the same data, except GetNeighbors won't include "box".
     * @param box A shape to check for containment.
     * @return An ArrayList of items contained roughly in the specified area.
     */
    public ArrayList<Shape> GetContainedItems(Shape box)
    {
        return GetContainedItems(new AABB(box.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight));
    }

    /**
     * Gets all the items located in a bounding box.
     * @param boundingBox The bounding box.
     * @return An ArrayList of items contained roughly in the specified area.
     */
    public ArrayList<Shape> GetContainedItems(AABB boundingBox)
    {
        ArrayList<Shape> items = new ArrayList<Shape>();

        for (int i : GetBuckets(boundingBox))
            items.addAll((buckets.get(i)));

        return items;
    }

    private ArrayList<Integer> GetBuckets(AABB bbox)
    {
        ArrayList<Integer> bucketIDs = new ArrayList<Integer>();

        //if the entire bounding box is outside of the scene, don't add any buckets.
        if (bbox.getLeftBound() > sceneWidth
            || bbox.getRightBound() < 0
            || bbox.getBottomBound() > sceneHeight
            || bbox.getTopBound() < 0)
            return bucketIDs;

        int idBL = GetBucketID(bbox.getLeftBound(), bbox.getBottomBound());
        int idTR = GetBucketID(bbox.getRightBound(), bbox.getTopBound());
        
        //Calculate columns/rows from bucket IDs.
        int colStart = idBL % cellsX;
        int colEnd = idTR % cellsX;
        int rowStart = idBL / cellsX;
        int rowEnd = idTR / cellsX;

        //iterate through all the containing tiles and add them.
        for (int i = rowStart; i <= rowEnd; i++)
        {
            for (int j = colStart; j <= colEnd; j++)
            {
                bucketIDs.add(i * cellsX + j);
            }
        }

        return bucketIDs;
    }
    
    public ArrayList<Shape> getBucketShapes(int x, int y)
    {
        return buckets.get(y * cellsX + x);
    }

    private int GetBucketID(Vector2 v)
    {
        return GetBucketID(v.x(), v.y());
    }

    private int GetBucketID(float x, float y)
    {
        int idX = (int)Clamp((int)(x / cellSizeX), 0, cellsX - 1);
        int idY = (int)Clamp((int)(y / cellSizeY), 0, cellsY - 1);

        return idY * cellsX + idX;
    }

    private Vector2 GetBucketLocation(int id)
    {
        return new Vector2(((id % cellsX) * cellSizeX), ((id / cellsX) * cellSizeY));
    }
    
    public void add(Shape element)
    {
        elements.add(element);
    }

    public void clear()
    {
        for (ArrayList<Shape> bucket : buckets.values())
            bucket.clear();
    }

    public boolean contains(Shape item)
    {
        return elements.contains(item);
    }

    //TODO also include a Count property that gets the number of buckets.
    public int getSize()
    {
       return elements.size();
    }

    public boolean remove(Shape item)
    {
        for (ArrayList<Shape> bucket : buckets.values())
            bucket.remove(item);

        return elements.remove(item);
    }
    
    private static float Clamp(float value, float min, float max)
    {
        return (value < min) ? min : ((value > max) ? max : value);
    }

}
