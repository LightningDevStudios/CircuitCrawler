package com.lds;

public class Animation 
{
	private Texture tex;
	private int framerate, xTiles, yTiles, startX, startY, curX, curY;
	private int animTimeMs;
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
		else
			animTimeMs = Stopwatch.elapsedTimeMs();
	}
	
	public void update()
	{
		//grab a time difference
		int timeElapsed = Stopwatch.elapsedTimeMs() - animTimeMs;
		
		//one set for time-based animation, another for framerate dependent animation
		if (!framerateDependent)
		{
			//make sure enough time has passed for the next frame to be drawn
			if (timeElapsed > framerate)
			{
				//take the amount of time that has passed, see if we need to skip a frame or two in order to keep up animation in low-FPS times
				int framesPassed = timeElapsed / (framerate);
				
				for(int i = 0; i < framesPassed; i++)
					incrementCount();
				
				animTimeMs = Stopwatch.elapsedTimeMs();
				
				//skip all the rows if we need to skip at least 1 row.
				/*while(framesPassed > xTiles)
				{
					//adjust the Y value
					if (curY == yTiles)
						curY = 0;
					else
						curY++;
					
					//decrement framesPassed
					framesPassed -= xTiles + 1;
				}
				
				//if we're near the end and there's enough for it to jump to the next row...
				if (framesPassed + curX > xTiles)
				{
					//subtract the frames from the current position to the end, leaving us with the position on the next row
					framesPassed -= xTiles - curX + 1;
					curX = framesPassed;
					
					//again, make sure we don't go over the row limit
					if (curY == yTiles)
						curY = 0;
					else
						curY++;
				}
				//regular increment
				else
					curX += framesPassed;
				
				//reset the timer
				startTimeMs = Stopwatch.elapsedTimeMs();*/
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
		return TilesetHelper.getTextureVertices(tex, curX + startX, curY + startY);
	}
}
