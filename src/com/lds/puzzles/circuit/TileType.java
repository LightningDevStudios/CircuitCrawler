package com.lds.puzzles.circuit;

import com.lds.Enums.Direction;

//\TODO HAX, get a proper UP/DOWN on the angled types...
public enum TileType 
{ 
	VERTICAL(Direction.UP, Direction.DOWN, 0), 
	HORIZONTAL(Direction.LEFT, Direction.RIGHT, 1),
	TOPRIGHT(Direction.RIGHT, Direction.DOWN, 2),
	BOTTOMRIGHT(Direction.RIGHT, Direction.UP, 3),
	BOTTOMLEFT(Direction.LEFT, Direction.UP, 4),
	TOPLEFT(Direction.DOWN, Direction.LEFT, 5);
	
	private Direction dir1, dir2;
	private int value;
	
	private TileType(Direction dir1, Direction dir2, int value)
	{
		this.dir1 = dir1;
		this.dir2 = dir2;
		this.value = value;
	}
	
	public Direction getOppositeDirection(Direction dir)
	{
		if (dir == dir1) 
			return dir2;
		else 
			return dir1;
	}
	
	public Direction getDir1()
	{
		return dir1;
	}
	
	public Direction getDir2()
	{
		return dir2;
	}
	
	public int getValue()
	{
		return value;
	}
}