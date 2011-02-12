package com.lds.puzzles.circuit;

import com.lds.Enums.Direction;

public class Tile 
{
	private Direction side1;
	private Direction side2;
	private boolean selected;
	private boolean highlighted;
	private boolean powered;
	public Tile (Direction _side1, Direction _side2)
	{
		selected = false;
		highlighted = false;
		powered = false;
		if (_side1 != _side2)
		{
			side1 = _side1;
			side2 = _side2;
		}
	}
	public void setTile (Tile newTile)
	{
		side1 = newTile.getSide1();
		side2 = newTile.getSide2();
	}
	//selected accessor
	public boolean isSelected ()
	{
		return selected;
	}
	//selected mutator (make true)
	public void select ()
	{
		selected = true;
	}
	//selected mutator (make false)
	public void deselect ()
	{
		selected = false;
	}
	//highlighted accessor
	public boolean isHightlighted ()
	{
		return highlighted;
	}
	//highlighted mutator (make true)
	public void highlight ()
	{
		highlighted = true;
	}
	//highlighted mutator (make false)
	public void dehighlight ()
	{
		highlighted = false;
	}
	//side1 accessor
	public Direction getSide1 ()
	{
		return side1;
	}
	//side2 accessor
	public Direction getSide2 ()
	{
		return side2;
	}
	//powered accessor
	public boolean isPowered ()
	{
		return powered;
	}
	//powered mutator
	public void setPower (boolean _powered)
	{
		powered = _powered;
	}
	//gets type based on in/out wires
	public int getType ()
	{
		//type 0 is a straight vertical piece
		if (side1 == Direction.UP && side2 == Direction.DOWN || side1 == Direction.DOWN && side2 == Direction.UP)
		{
			return 0;
		}
		//type 1 is a straight horizontal line
		else if (side1 == Direction.LEFT && side2 == Direction.RIGHT || side1 == Direction.RIGHT && side2 == Direction.LEFT)
		{
			return 1;
		}
		//type 2 is a bottom-left corner (L shape)
		else if (side1 == Direction.UP && side2 == Direction.RIGHT || side1 == Direction.RIGHT && side2 == Direction.UP)
		{
			return 2;
		}
		//type 3 is a top-left corner
		else if (side1 == Direction.DOWN && side2 == Direction.RIGHT || side1 == Direction.RIGHT && side2 == Direction.DOWN)
		{
			return 3;
		}
		//type 4 is a top-right corner
		else if (side1 == Direction.DOWN && side2 == Direction.LEFT || side1 == Direction.LEFT && side2 == Direction.DOWN)
		{
			return 4;
		}
		//type 5 is a bottom-right corner
		else
		{
			return 5;
		}
	}
	//flips directtion of tile (right to left, up to down, etc.)
	public static Direction returnFlippedTile (Direction dir)
	{
		switch (dir)
		{
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
			default:
				return Direction.UP;
		}
	}
	
	//returns x position of next tile in powering sequence
	public static int returnNewXPos (int x, Direction dir)
	{
		if (dir == Direction.LEFT)
			return x - 1;
		else if (dir == Direction.RIGHT)
			return x + 1;
		else
			return x;
	}
	
	//return y position of next tile in powering sequence
	public static int returnNewYPos (int y, Direction dir)
	{
		if (dir == Direction.UP)
			return y - 1;
		else if (dir == Direction.DOWN)
			return y + 1;
		else
			return y;
	}
	public String toString ()
	{
		String output = "";
		output +=( "[ " + side1 + ", " + side2 + ", ");
		if (selected == true)
			output += ("selected, ");
		if (highlighted == true)
			output += ("highlighted, ");
		if (powered == true)
			output += ("powered, ");
		output += "]";
		return output;
	}
}
