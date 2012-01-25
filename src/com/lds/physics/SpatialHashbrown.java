package com.lds.physics;

import com.lds.physics.primatives.Shape;
import com.lds.math.*;

import java.util.ArrayList;

public class SpatialHashbrown
{
    public Vector2 size;
    public ArrayList<Shape> shapes;

    private int gridSize;
    public GridNode[][] grid;

    public SpatialHashbrown(Vector2 size, ArrayList<Shape> shapes, int gridSize)
    {
        this.size = size;
        this.shapes = shapes;
        if (gridSize == 0)
            this.gridSize = (int)(size.x() + size.y()) / 200;
        else
            this.gridSize = gridSize;

        grid = new GridNode[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                grid[i][j] = new GridNode();
            }
        }
    }

    public void Fry()
    {
        ArrayList<Shape> removeList = new ArrayList<Shape>();
        for (Shape s : shapes)
        {
            AABB box = new AABB(s.getWorldVertices());
            int top = (int)((-box.getTopBound() + 0.5f * size.y()) / (size.y() / gridSize));
            int bottom = (int)((-box.getBottomBound() + 0.5f * size.y()) / (size.y() / gridSize));
            int left = (int)((box.getLeftBound() + 0.5f * size.x()) / (size.x() / gridSize));
            int right = (int)((box.getRightBound() + 0.5f * size.x()) / (size.x() / gridSize));

            for (int i = top; i <= bottom; i++)
            {
                for (int j = left; j <= right; j++)
                {
                    try
                    {
                        grid[i][j].addShape(s);
                    }
                    catch (Exception e)
                    {
                        removeList.add(s);
                    }
                }
            }
        }

        if (removeList.size() > 0)
            for (Shape s : removeList)
                shapes.remove(s);
    }

    public ArrayList<Contact> OmNomNom()
    {
        ArrayList<Contact> pairs = new ArrayList<Contact>();
        for (GridNode[] nodes : grid)
        {
            for (GridNode node : nodes)
            {
                if (shapes.size() < 2)
                    continue;
    
                for (int i = 0; i < node.size() - 1; i++)
                {
                    for (int j = i + 1; j < node.size(); j++)
                    {
                        pairs.Add(new Contact(node.get(i), node.get(j)));
                    }
                }
                
                node.clear();
            }
        }

        return pairs;
    }
}