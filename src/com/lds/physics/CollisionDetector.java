package com.lds.physics;

import java.util.ArrayList;

import com.lds.math.*;
import com.lds.game.entity.Entity;

public class CollisionDetector 
{	
	private Grid masterGrid;
	
	private Vector2 size;
	private ArrayList<Entity> entList;
	
	public CollisionDetector(Vector2 size, ArrayList<Entity> entList) 
	{
		this.size = size;
		this.entList = entList;
	}
	
	public void Run()
	{
		masterGrid = new Grid(size, new Vector2(0,0), 0, entList);	
		masterGrid.FindAndUpdateEntitiesInAQuadrant(masterGrid);
	}
	
	public Grid getMaster() { return masterGrid; }
}