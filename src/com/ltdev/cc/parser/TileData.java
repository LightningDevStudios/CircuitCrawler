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

package com.ltdev.cc.parser;

import android.graphics.Point;

import com.ltdev.cc.Tile;
import com.ltdev.cc.Tile.TileType;

import javax.microedition.khronos.opengles.GL11;

public class TileData
{
	private int tilePosX, tilePosY;
	private Tile tile;
	
	public TileData(GL11 gl, String state, int x, int y, int tilesetX, int tilesetY)
	{
		this.tilePosX = x;
		this.tilePosY = y;
		
		TileType tState = TileType.FLOOR;
		
		if (state.equalsIgnoreCase("floor"))
            tState = TileType.FLOOR;
        else if (state.equalsIgnoreCase("wall"))
            tState = TileType.WALL;
        else if (state.equalsIgnoreCase("pit"))
            tState = TileType.PIT;
        else if (state.equalsIgnoreCase("slip"))
            tState = TileType.SLIP;
				
		tile = new Tile(new Point(tilePosX, tilePosY), tilesetY, tilesetX, tState);
	}
	
	/**
	 * Gets the contained Tile instance.
	 * @return An instance of the tile.
	 */
	public Tile getTile()
	{
	    return tile;
	}
}
