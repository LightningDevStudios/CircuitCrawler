package com.lds.game;

import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//constants
	public static final float DEFAULT_SPEED = 20.0f;

	//interpolation data
	public float interpX, interpY, interpXScl, interpYScl, interpAngle;
	public float endX, endY, endXScl, endYScl, endAngle, speed;
	public int interpMoveTimeMs, interpRotTimeMs, interpSclTimeMs;
	private float moveX, moveY;
	private float moveSpeed, rotSpeed, sclSpeed;
	private boolean isInterpTrans, isInterpRot, isInterpScl;
	
	public PhysEnt (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl, float _speed)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, true, RenderMode.TILESET);
		
		//initialize interpolation variables
		endX = xPos;
		endY = yPos;
		endXScl = xScl;
		endYScl = yScl;
		endAngle = angle;
		moveSpeed = 100.0f;
		rotSpeed = 90.0f;
		isSolid = true;
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
		if (interpX == 0) { interpX = 0.1f; }
		if (interpY == 0) { interpY = 0.1f; }
		double theta = Math.atan((double)interpY/(double)interpX);
		moveX = (float)Math.cos(theta);
		moveY = (float)Math.sin(theta);
		endX = x;
		endY = y;
		isInterpTrans = true;
		
		interpMoveTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	//sets angle of an entity to a new value
	public void rotateTo (float degrees)
	{
		endAngle = degrees;
		interpAngle = degrees - angle;
		isInterpRot = true;
		
		interpRotTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (float x, float y)
	{
		interpXScl = x - xScl;
		interpYScl = y - yScl;
		endXScl = x;
		endYScl = y;
		isInterpScl = true;

		interpSclTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		interpX = x;
		interpY = y;
		endX = xPos + x;
		endY = yPos + y;
		isInterpTrans = true;

		interpMoveTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (float degrees)
	{
		endAngle = angle + degrees;
		interpAngle = degrees;	
		isInterpRot = true;
		interpRotTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	//scales relative to current scaling
	//i.e. if ent1 is scaled (2.0f, 2.0f), if you do ent1.scale(3.0f, 3.0f) the final scaling will be (6.0f, 6.0f)
	public void scale (float x, float y)
	{
		endXScl = xScl * x;
		endYScl = yScl * y;
		interpXScl = endXScl - xScl;
		interpYScl = endYScl - yScl;
		isInterpScl = true;
		
		interpSclTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	public void stop ()
	{
		if (isInterpTrans)
		{

			xPos -= speed / 1000 * (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs) * interpX;
			yPos -= (speed / 10) * interpY;
			isInterpTrans = false;
		}
		
		if (isInterpRot)
		{
			angle -= speed / 1000 * (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs);
			isInterpRot = false;
		}
		
		if (isInterpScl)
		{
			xScl -= (speed / 10) * interpXScl;
			yScl -= (speed / 10) * interpYScl;
			isInterpScl = false;
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
	
	public void moveInterpolate ()
	{	
		//if the object needs to be interpolated
		if (isInterpTrans)
		{		
			//increments movement
			//TODO find a good speed scale, using 0.05f is too precise to get a clear reading on in error check.
			float interval = (float)(Stopwatch.elapsedTimeInMilliseconds() - interpMoveTimeMs);
			xPos += moveSpeed / 1000 * interval * moveX;
			yPos += moveSpeed / 1000 * interval * moveY;
			
			//error check
			if (xPos <= endX + (moveSpeed /2000 * interval * moveX) && xPos >= endX - (moveSpeed /2000 * interval * moveX) || yPos <= endY + (moveSpeed / 2000 * interval * moveY) && yPos >= endY - (moveSpeed / 2000 * interval * moveY))
			{
				xPos = endX;
				yPos = endY;
				isInterpTrans = false;
				isRendered = true;
			}
			interpMoveTimeMs = Stopwatch.elapsedTimeInMilliseconds();
		}
	}
	
	public void rotateInterpolate ()
	{
		if (isInterpRot)
		{
			//if the angle is greater than 360, set it back to within 360
			if (angle >= 360.0f)
			{
				angle -= 360.0f * ((int)angle / 360);
			}
			else if (angle < 0.0f)
			{
				angle = 0.0f; //just in case timer freaks out and goes to a very large negative number
			}
			//increments angle
			float increment = (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs);
			angle += rotSpeed / 1000 * increment; //speed is degrees per second, time in milliseconds, so divide by 1000
			System.out.println(increment + " " + angle);
			//error check
			if (angle <= endAngle + (rotSpeed / 2000 * increment) && angle >= endAngle - (rotSpeed / 2000 * increment))
			{
				angle = endAngle;
				isInterpRot = false;
			}
			
			interpRotTimeMs = Stopwatch.elapsedTimeInMilliseconds();
			
		}
	}
	
	public void scaleInterpolate ()
	{
		if (isInterpScl)
		{				
			//increments scaling
			xScl += speed * interpXScl;
			yScl += speed * interpYScl;
			
			//error check
			if (xScl <= endXScl + (speed / 6000) && xScl >= endXScl - (speed / 6000))
			{
				xScl = endXScl;
				isInterpScl = false;
			}
			/*if (ent.yScl <= ent.endYScl + ent.speed / 2 && ent.yScl >= ent.endYScl - ent.speed / 2)
			{
				ent.yScl = ent.endYScl;
				isInterpScl = false;
			}*/
		}
	}
}
