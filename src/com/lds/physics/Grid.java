package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

public class Grid 
{
	private ArrayList<GridBox> grids = new ArrayList<GridBox>();
	private ArrayList<Entity> entList;
	private Grid[] subGrids = new Grid[4];
	
	private ArrayList<ArrayList<Entity>> colEnts = new ArrayList<ArrayList<Entity>>();
	private ArrayList<ArrayList<Entity>> colEntsOnLines = new ArrayList<ArrayList<Entity>>();
	
	private int level;
	private Vector2 size;
	private Vector2 center;
	private final int MAX_LEVEL = 5;
	private Grid parentGrid;
	
	public Grid(Vector2 size, Vector2 center, int level, ArrayList<Entity> entList, Grid parentGrid)
	{
		this.parentGrid = parentGrid;
		this.level = level;
		this.size = size;
		this.center = center;
		this.entList = entList;
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
			
			grid.add(new GridBox(new Vector4(X1, X2, Y1, Y2), boxPosList[i], new Vector2(gridSizeX, gridSizeY), i));
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

	public void SplitGrid(int quadrant) 
	{
		switch(quadrant)
		{
			case 1:
				subGrids[0] = new Grid(grids.get(0).gridSize, grids.get(0).center, level + 1, grids.get(0).detectedEnts, this);
				FindAndUpdateEntitiesInAQuadrant(subGrids[0]);
			break;
			case 2:
				subGrids[1] = new Grid(grids.get(1).gridSize, grids.get(1).center, level + 1, grids.get(1).detectedEnts, this);
				FindAndUpdateEntitiesInAQuadrant(subGrids[1]);
			break;
			case 3:
				subGrids[2] = new Grid(grids.get(2).gridSize, grids.get(2).center, level + 1, grids.get(2).detectedEnts, this);
				FindAndUpdateEntitiesInAQuadrant(subGrids[2]);
			break;
			case 4:
				subGrids[3] = new Grid(grids.get(3).gridSize, grids.get(3).center, level + 1, grids.get(3).detectedEnts, this);
				FindAndUpdateEntitiesInAQuadrant(subGrids[3]);
			break;
		}
		
		
	}
	
	public void FindAndUpdateEntitiesInAQuadrant(Grid grid) //TODO: SHORTER NAME PL0X
	{
		for(Entity ent : grid.entList)
		{
			boolean isOnALine = true;
			for(GridBox bx : grid.getGrids())
			{
				if(bx.IsInBox(ent))
				{
					bx.entityCount++;
					isOnALine = false;
				}
			}
			if(isOnALine)
			{
				ArrayList<Entity> onLineEnts = new ArrayList<Entity>();
				onLineEnts.add(ent);
				for(Entity ent2 : grid.entList)
				{
					onLineEnts.add(ent2);
				}
				colEntsOnLines.add(onLineEnts);
			}
		}
		
		ChainUpOnLineEnts(colEntsOnLines);
		
		for(GridBox bx : grid.getGrids())
		{
			if(bx.entityCount > 1)
			{
				if(grid.level <= MAX_LEVEL)
					grid.SplitGrid(bx.quadrant);
				else
				{
					colEnts.add(bx.detectedEnts);
				}
			}
		}
		
		if(colEnts.size() > 0)
			ChainUpNormalEnts(colEnts);
	}
	
	public void ChainUpNormalEnts(ArrayList<ArrayList<Entity>> colEnts)
	{
		if(parentGrid == null)
			this.colEnts = colEnts;
		parentGrid.ChainUpNormalEnts(colEnts);
	}
	
	public void ChainUpOnLineEnts(ArrayList<ArrayList<Entity>> colEntsOnLines)
	{
		if(parentGrid == null)
			this.colEntsOnLines = colEntsOnLines;
		parentGrid.ChainUpOnLineEnts(colEntsOnLines);
	}
	
	public void Clear()
	{
		for(int i = 0; i < 4; i++)
			subGrids[i].Clear();
		
		grids.clear();
		
		for(int i = 0; i < 4; i++)
			subGrids[i] = null;
	}
	
	public ArrayList<Entity> getEntList() { return entList; }
	public void setEntList(ArrayList<Entity> entList) { this.entList = entList; }
}
