package com.lds.puzzles.circuit;

import com.lds.puzzles.circuit.dir;

public class Tile 
{
	private dir side1;
	private dir side2;
	private boolean selected;
	private boolean highlighted;
	private boolean powered;
	public Tile (dir _side1, dir _side2)
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
	public dir getSide1 ()
	{
		return side1;
	}
	//side2 accessor
	public dir getSide2 ()
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
		if (side1 == dir.up && side2 == dir.down || side1 == dir.down && side2 == dir.up)
		{
			return 0;
		}
		//type 1 is a straight horizontal line
		else if (side1 == dir.left && side2 == dir.right || side1 == dir.right && side2 == dir.left)
		{
			return 1;
		}
		//type 2 is a bottom-left corner (L shape)
		else if (side1 == dir.up && side2 == dir.right || side1 == dir.right && side2 == dir.up)
		{
			return 2;
		}
		//type 3 is a top-left corner
		else if (side1 == dir.down && side2 == dir.right || side1 == dir.right && side2 == dir.down)
		{
			return 3;
		}
		//type 4 is a top-right corner
		else if (side1 == dir.down && side2 == dir.left || side1 == dir.left && side2 == dir.down)
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
	public static dir returnFlippedTile (dir input)
	{
		switch (input)
		{
			case up:
				return dir.down;
			case down:
				return dir.up;
			case left:
				return dir.right;
			case right:
				return dir.left;
			default:
				return dir.up;
		}
	}
	
	//returns x position of next tile in powering sequence
	public static int returnNewXPos (int x, dir input)
	{
		if (input == dir.left)
			return x - 1;
		else if (input == dir.right)
			return x + 1;
		else
			return x;
	}
	
	//return y position of next tile in powering sequence
	public static int returnNewYPos (int y, dir input)
	{
		if (input == dir.up)
			return y - 1;
		else if (input == dir.down)
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
