package com.lds.physics;

import com.lds.physics.primatives.Shape;
import com.lds.math.*;

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

    /// <summary>
    /// Clears and re-creates the grid. Faster than updating all existing items.
    /// </summary>
    public void populate()
    {
        clear();

        for (Shape item : elements)
        {
            AABB bbox = new AABB(item.getWorldVertices());
            bbox.convertToBottomLeftCoords(sceneWidth, sceneHeight);
            for (int i : GetBuckets(bbox))
            {
                buckets.get(i).add(item);
            }
        }
    }

    /// <summary>
    /// Gets every possible pair of neighbors.
    /// </summary>
    /// <returns>A ArrayList of neighbor pairs.</returns>
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

    /// <summary>
    /// Gets all the items that are near a specified item.
    /// </summary>
    /// <param name="item">An item contained in the grid.</param>
    /// <returns>All of the items near item.</returns>
    public ArrayList<Shape> GetNeighbors(Shape item)
    {
        ArrayList<Shape> neighbors = new ArrayList<Shape>();

        if (!elements.contains(item))
            return null;
        
        AABB bbox = new AABB(item.getWorldVertices());
        bbox.convertToBottomLeftCoords(sceneWidth, sceneHeight);
        for (int i : GetBuckets(bbox))
        {
            neighbors.addAll(buckets.get(i));
            neighbors.remove(item);
        }

        return neighbors;
    }

    /// <summary>
    /// Gets all the items located in the box centered at a point with a specified size.
    /// </summary>
    /// <param name="point">The center of the area to check.</param>
    /// <param name="size">The size of the area to check.</param>
    /// <returns>A ArrayList of items contained roughly in the specified area.</returns>
    public ArrayList<Shape> GetContainedItems(Shape s, Vector2 size)
    {
        float x = s.getPos().x();
        float y = s.getPos().y();

        float left = x - size.x() / 2;
        float right = x + size.x() / 2;
        float top = y + size.y() / 2;
        float bottom = y - size.y() / 2;

        AABB bbox = new AABB(left, right, top, bottom);
        bbox.convertToBottomLeftCoords(sceneWidth, sceneHeight);
        return GetContainedItems(bbox);
    }

    /// <summary>
    /// Gets all the items located in a bounding box.
    /// </summary>
    /// <remarks>
    /// This method assumes that item is not contained in the grid, unlike <see cref="GetNeighbors"/>.
    /// Assuming box is of the type Shape, this method and GetNeighbors will return the same data, except GetNeighbors won'Shape include "box".
    /// </remarks>
    /// <param name="box">An object that has a bounding box.</param>
    /// <returns>A ArrayList of items contained roughly in the specified area.</returns>
    public ArrayList<Shape> GetContainedItems(Shape box)
    {
        AABB bbox = new AABB(box.getWorldVertices());
        bbox.convertToBottomLeftCoords(sceneWidth, sceneHeight);
        return GetContainedItems(bbox);
    }

    /// <summary>
    /// Gets all the items located in a bounding box.
    /// </summary>
    /// <param name="boundingBox">The bounding box.</param>
    /// <returns>A ArrayList of items contained roughly in the specified area.</returns>
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
        int id = GetBucketID(x, y);
        return buckets.get(id);
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
