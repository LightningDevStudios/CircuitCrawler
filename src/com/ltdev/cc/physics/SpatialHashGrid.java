/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.*;
import com.ltdev.physics.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A broadphase that hashes a collection of objects into buckets of neighboring objects to quickly look up objects near each other.
 * @author Lightning Development Studios
 */
public class SpatialHashGrid
{
    private float sceneWidth, sceneHeight;
    private int cellsX, cellsY;
    private float cellSizeX, cellSizeY;

    private ArrayList<Shape> elements;
    private HashMap<Integer, ArrayList<Shape>> buckets;

    /**
     * Initializes a new instance of the SpatialHashGrid class.
     * @param elements A collection of objects to initialize the grid with.
     * @param sceneWidth The maximum width of the grid.
     * @param sceneHeight The maximum height of the grid.
     * @param cellsX The number of cells to divide the grid in on the X axis.
     * @param cellsY The number of cells to divide the grid in on the Y axis.
     */
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

    /**
     * Adds a collection of objects to the grid.
     * @param elements The collection of objects to add.
     */
    public void addRange(ArrayList<Shape> elements)
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

            for (int i : getBuckets(new AABB(item.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight)))
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
    public ArrayList<Shape> getNeighbors(Shape item)
    {
        ArrayList<Shape> neighbors = new ArrayList<Shape>();

        if (!elements.contains(item))
            return null;
        
        for (int i : getBuckets(new AABB(item.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight)))
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
    public ArrayList<Shape> getContainedItems(Vector2 point, Vector2 size)
    {
        float x = point.x();
        float y = point.y();

        float halfX = sceneWidth / 2;
        float halfY = sceneHeight / 2;
        
        float left = x - size.x() / 2 + halfX;
        float right = x + size.x() / 2 + halfX;
        float top = y + size.y() / 2 + halfY;
        float bottom = y - size.y() / 2 + halfY;
        
        return getContainedItems(new AABB(left, right, top, bottom));
    }

    /**
     * Gets all the items located in a bounding box.
     * 
     * This method assumes that item is not contained in the grid, unlike GetNeighbors.
     * Assuming box is of the type Shape, this method and GetNeighbors will return the same data, except GetNeighbors won't include "box".
     * @param box A shape to check for containment.
     * @return An ArrayList of items contained roughly in the specified area.
     */
    public ArrayList<Shape> getContainedItems(Shape box)
    {
        return getContainedItems(new AABB(box.getWorldVertices()).toBottomLeftCoords(sceneWidth, sceneHeight));
    }

    /**
     * Gets all the items located in a bounding box.
     * @param boundingBox The bounding box.
     * @return An ArrayList of items contained roughly in the specified area.
     */
    public ArrayList<Shape> getContainedItems(AABB boundingBox)
    {
        ArrayList<Shape> items = new ArrayList<Shape>();

        for (int i : getBuckets(boundingBox))
            items.addAll(buckets.get(i));

        return items;
    }

    /**
     * Gets the IDs of all the buckets that contain a bounding box.
     * @param bbox The bounding box.
     * @return All the bucket IDs that contain the bounding box.
     */
    private ArrayList<Integer> getBuckets(AABB bbox)
    {
        ArrayList<Integer> bucketIDs = new ArrayList<Integer>();

        //if the entire bounding box is outside of the scene, don't add any buckets.
        if (bbox.getLeftBound() > sceneWidth
            || bbox.getRightBound() < 0
            || bbox.getBottomBound() > sceneHeight
            || bbox.getTopBound() < 0)
            return bucketIDs;

        int idBL = getBucketID(bbox.getLeftBound(), bbox.getBottomBound());
        int idTR = getBucketID(bbox.getRightBound(), bbox.getTopBound());
        
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
    
    /**
     * Gets the objects contained in the cell at (x, y).
     * @param x The X coordinate of the cell.
     * @param y The Y coordinate of the cell.
     * @return A collection of objects near each other.
     */
    public ArrayList<Shape> getBucketShapes(int x, int y)
    {
        return buckets.get(y * cellsX + x);
    }

    /**
     * Converts 2d coordinates to bucket IDs.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The bucket ID that contains the point.
     */
    private int getBucketID(float x, float y)
    {
        int idX = (int)clamp((int)(x / cellSizeX), 0, cellsX - 1);
        int idY = (int)clamp((int)(y / cellSizeY), 0, cellsY - 1);

        return idY * cellsX + idX;
    }

    /**
     * Adds an object to the grid.
     * @param element The object to add.
     */
    public void add(Shape element)
    {
        elements.add(element);
    }

    /**
     * Clears all objects from the grid.
     */
    public void clear()
    {
        for (ArrayList<Shape> bucket : buckets.values())
            bucket.clear();
    }

    /**
     * Checks whether or not the object is part of the grid.
     * @param item The item to check for containment.
     * @return A value indicating whether the object is part of the grid or not.
     */
    public boolean contains(Shape item)
    {
        return elements.contains(item);
    }

    /**
     * Gets the number of objects in the grid.
     * @return The number of objects in the grid.
     */
    public int getSize()
    {
       return elements.size();
    }

    /**
     * Removes an object from the grid.
     * @param item The item to remove.
     * @return A value indicating whether the item was removed or not.
     */
    public boolean remove(Shape item)
    {
        for (ArrayList<Shape> bucket : buckets.values())
            bucket.remove(item);

        return elements.remove(item);
    }
    
    /**
     * Clamps a value between a minimum and a maximum.
     * @param value The value to clamp.
     * @param min The minimum the value can be.
     * @param max The maximum the value can be.
     * @return min, max, or value depending on what value is.
     */
    private static float clamp(float value, float min, float max)
    {
        return (value < min) ? min : ((value > max) ? max : value);
    }
    
    /**
     * Gets the number of cells in the X direction.
     * @return The number of cells in the X direction.
     */
    public int getCellsX()
    {
        return cellsX;
    }
    
    /**
     * Gets the number of cells in the Y direction.
     * @return The number of cells in the Y direction.
     */
    public int getCellsY()
    {
        return cellsY;
    }

    /**
     * Gets the size of one cell in the X direction.
     * @return The X size of a cell.
     */
    public float getCellSizeX()
    {
        return cellSizeX;
    }
    
    /**
     * Gets the size of one cell in the Y direction.
     * @return The Y size of a cell.
     */
    public float getCellSizeY()
    {
        return cellSizeY;
    }
}
