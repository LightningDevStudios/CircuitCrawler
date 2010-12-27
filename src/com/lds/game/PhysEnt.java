package com.lds.game;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//interpolation data
	public float interpX, interpY, interpXScl, interpYScl, interpAngle, endX, endY, endXScl, endYScl, endAngle;
	public boolean shouldBreak;
	
	public PhysEnt (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl, float _speed)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, true, _speed);
		
		//initialize interpolation variables
		endX = xPos;
		endY = yPos;
		endXScl = xScl;
		endYScl = yScl;
		endAngle = angle;
		shouldBreak = false;
	}
	
	public void renderNextFrame()
	{
	}
	
	/***************************************
	 * Interpolated Transformation Methods *
	 ***************************************/
	
	//sets position to to new x and y and interpolates
	public void moveTo (float x, float y)
	{
		//calculates x and y distance of movement
		interpX = (x - xPos);
		interpY = (y - yPos);
		endX = x;
		endY = y;
	}
	
	//sets angle of an entity to a new value
	public void rotateTo (float degrees)
	{
		endAngle = degrees;
		interpAngle = degrees - angle;
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (float x, float y)
	{
		interpXScl = x - xScl;
		interpYScl = y - yScl;
		endXScl = x;
		endYScl = y;
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		interpX = x;
		interpY = y;
		endX = xPos + x;
		endY = yPos + y;
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (float degrees)
	{
		endAngle = angle + degrees;
		interpAngle = degrees;	
	}
	
	//scales relative to current scaling
	//i.e. if ent1 is scaled (2.0f, 2.0f), if you do ent1.scale(3.0f, 3.0f) the final scaling will be (6.0f, 6.0f)
	public void scale (float x, float y)
	{
		endXScl = xScl * x;
		endYScl = yScl * y;
		interpXScl = endXScl - xScl;
		interpYScl = endYScl - yScl;
	}
	
	public void stop ()
	{
		if ((xPos != endX || yPos != endY))
		{
			xPos -= speed * interpX;
			yPos -= speed * interpY;
			endX = xPos;
			endY = yPos;
			shouldBreak = true;
		}
		
		if (angle != endAngle)
		{
			angle -= speed * interpAngle;
			endAngle = angle;
			shouldBreak = true;
		}
		
		if ((xScl != endXScl) || (yScl != endYScl))
		{
			xScl -= speed * interpXScl;
			yScl -= speed * interpYScl;
			endXScl = xScl;
			endYScl = yScl;
			shouldBreak = true;
		}
	}
	
	/**********************************
	 * Instant Transformation Methods *
	 **********************************/
	
	//mutator for position
	public void setPos (float x, float y)
	{
		xPos = x;
		yPos = y;
		endX = x;
		endY = y;
	}
	
	//mutator for angle
	public void setAngle (float degrees)
	{
		angle = degrees;
		endAngle = degrees;
	}
	
	//mutator for scale
	public void setScale (float x, float y)
	{
		xScl = x;
		yScl = y;
		endXScl = x;
		endYScl = y;
	}
}