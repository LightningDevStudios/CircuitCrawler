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

package com.ltdev.cc.puzzles.circuit;

import com.ltdev.Direction;

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
