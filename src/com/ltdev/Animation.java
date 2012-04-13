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

package com.ltdev;

import android.graphics.Point;

public class Animation 
{
	private Texture tex;
	private int framerate, xTiles, yTiles, startX, startY, curX, curY;
	private boolean framerateDependent;
	
	public Animation(Texture tex, int xTiles, int yTiles, int startX, int startY, int framerate)
	{
		this.tex = tex;
		
		this.xTiles = xTiles;
		this.yTiles = yTiles;
		
		this.startX = startX;
		this.startY = startY;
		
		curX = 0;
		curY = 0;
		
		this.framerate = framerate;
		
		if (framerate < 0)
			framerateDependent = true;
	}
	
	public void update()
	{
		//grab a time difference
		int timeElapsed = (int)Stopwatch.getFrameTime();
		
		//one set for time-based animation, another for framerate dependent animation
		if (!framerateDependent)
		{
			//make sure enough time has passed for the next frame to be drawn
			if (timeElapsed > framerate)
			{
				//take the amount of time that has passed, see if we need to skip a frame or two in order to keep up animation in low-FPS times
				int framesPassed = timeElapsed / framerate;
				
				for (int i = 0; i < framesPassed; i++)
					incrementCount();
			}
		}
		
		//Increments each frame
		else
		{
			incrementCount();
		}
	}
	
	private void incrementCount()
	{
		if (curX == xTiles)
		{
			if (curY == yTiles)
				curY = 0;
			else
				curY++;
			
			curX = 0;
		}
		
		else
			curX++;
	}
	
	public void reset()
	{
		curX = 0;
		curY = 0;
	}
	
	public void setX(int x)
	{
		if (x > 0 && x <= xTiles)
			curX = x;
	}
	
	public void setY(int y)
	{
		if (y > 0 && y <= yTiles)
			curY = y;
	}
	
	public float[] getCurrentFrame()
	{
		return TilesetHelper.getTextureVertices(tex, new Point(curX + startX, curY + startY));
	}
}
