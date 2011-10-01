package com.lds.physics;

import java.util.ArrayList;

import com.lds.math.Vector2;
import com.lds.math.Vector4;

public class Grid 
{
	private ArrayList<GridBox> grids = new ArrayList<GridBox>();
	private Grid[] subGrids = new Grid[4];
	
	private int level;
	private Vector2 size;
	private Vector2 center;
	
	public Grid(Vector2 size, Vector2 center, int level)
	{
		this.level = level;
		this.size = size;
		this.center = center;
		
		setGrids(CreateGrid(size, center));
	}
	
	//Creates a grid of 4 boxes
	private ArrayList<GridBox> CreateGrid(Vector2 size, Vector2 center)
	{
		ArrayList<GridBox> grid = new ArrayList<GridBox>();
		int gridSizeX = (int) (size.getX()/2);
		int gridSizeY = (int) (size.getY()/2);
		
		//Calculate box centers
		Vector2[] boxPosList = 
		{
			new Vector2(-size.getX()/4, size.getY()/4),	
			new Vector2( center.getX(), size.getY()/4),
			new Vector2( size.getX()/4, size.getY()/4),
			new Vector2(-size.getX()/4, center.getY()),
		};
		
		//Calculate box sides
		for(int i = 1; i < boxPosList.length - 1; i++)
		{
			float X1 = boxPosList[i].getX() - gridSizeX/2;
			float Y1 = boxPosList[i].getY() + gridSizeY/2;
			float X2 = boxPosList[i].getX() + gridSizeX/2;
			float Y2 = boxPosList[i].getY() - gridSizeY/2;
			
			grid.add(new GridBox(new Vector4(X1, X2, Y1, Y2), boxPosList[i], new Vector2(gridSizeX, gridSizeY), "Q" + i));
		}
		return grid;
	}

	public ArrayList<GridBox> getGrids() 
	{
		return grids;
	}

	public void setGrids(ArrayList<GridBox> grids) 
	{
		this.grids = grids;
	}

	public void SplitGrid(String str) 
	{
		switch(str)
		{
			case "Q1":
				subGrids[0] = new Grid(grids.get(0).gridSize, grids.get(0).center, level + 1);
			break;
			case "Q2":
				subGrids[1] = new Grid(grids.get(1).gridSize, grids.get(1).center, level + 1);
			break;
			case "Q3":
				subGrids[2] = new Grid(grids.get(2).gridSize, grids.get(2).center, level + 1);
			break;
			case "Q4":
				subGrids[3] = new Grid(grids.get(3).gridSize, grids.get(3).center, level + 1);
			break;
		}
	}
}
