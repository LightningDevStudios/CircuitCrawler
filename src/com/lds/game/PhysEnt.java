package com.lds.game;

import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//interpolation data
	public float interpX, interpY, interpXScl, interpYScl, interpAngle;
	public float endX, endY, endXScl, endYScl, endAngle;
	public int interpMoveTimeMs, interpRotTimeMs, interpSclTimeMs;
	private float moveX, moveY, scaleX, scaleY;
	protected float moveSpeed;
	protected float rotSpeed;
	protected float sclSpeed;
	private boolean isInterpTrans, isInterpRot, isInterpScl;
	private boolean isRotatingCCW;
	
	public PhysEnt(float size, float xPos, float yPos, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, renderMode, moveSpeed, rotSpeed, sclSpeed);
	}
	
	public PhysEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, renderMode);
		
		//initialize interpolation variables
		endX = xPos;
		endY = yPos;
		endXScl = xScl;
		endYScl = yScl;
		endAngle = angle;
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
	}
	
	@Override
	public void update()
	{
		moveInterpolate();
		rotateInterpolate();
		scaleInterpolate();
	}
	
	@Override
	public void collide(Entity ent)
	{
		this.stop();
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
		if (interpX == 0) { interpX = 0.001f; }
		if (interpY == 0) { interpY = 0.001f; }
		double theta = Math.atan2((double)interpY, (double)interpX);
		if (theta < 0)
			theta += 2 * Math.PI;
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
		if (!isInterpRot)
		{
		endAngle = degrees;
		float dist = degrees - angle;
		
		if (dist >= 180 || dist >= -180 && dist <= 0)
			isRotatingCCW = false;
		else
			isRotatingCCW = true;
		
		if (!(dist == 0))
			isInterpRot = true;
		
		interpRotTimeMs = Stopwatch.elapsedTimeInMilliseconds();
		}
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (float x, float y)
	{
		interpXScl = x - xScl;
		interpYScl = y - yScl;
		if (interpXScl == 0) { interpXScl = 0.1f; }
		if (interpYScl == 0) { interpYScl = 0.1f; }
		double ratio = Math.atan2((double)interpYScl, (double)interpXScl);
		if (ratio < 0)
			ratio += 2 * Math.PI;
		scaleX = (float)Math.cos(ratio);
		scaleY = (float)Math.sin(ratio);
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
		if (interpX == 0) { interpX = 0.1f; }
		if (interpY == 0) { interpY = 0.1f; }
		double theta = Math.atan2((double)interpY, (double)interpX);
		if (theta < 0)
			theta += 2 * Math.PI;
		moveX = (float)Math.cos(theta);
		moveY = (float)Math.sin(theta);
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
		if (interpXScl == 0) { interpXScl = 0.001f; }
		if (interpYScl == 0) { interpYScl = 0.001f; }
		double ratio = Math.atan2((double)interpYScl, (double)interpXScl);
		if (ratio < 0)
			ratio += 2 * Math.PI;
		scaleX = (float)Math.cos(ratio);
		scaleY = (float)Math.sin(ratio);
		interpXScl = endXScl - xScl;
		interpYScl = endYScl - yScl;
		isInterpScl = true;
		
		interpSclTimeMs = Stopwatch.elapsedTimeInMilliseconds();
	}
	
	public void stop ()
	{
		if (isInterpTrans)
		{

			xPos -= moveSpeed / 1000 * (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs) * interpX;
			yPos -= (moveSpeed / 10) * interpY;
			isInterpTrans = false;
		}
		
		if (isInterpRot)
		{
			angle -= rotSpeed / 1000 * (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs);
			isInterpRot = false;
		}
		
		if (isInterpScl)
		{
			xScl -= (sclSpeed / 10) * interpXScl;
			yScl -= (sclSpeed / 10) * interpYScl;
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
		Game.worldOutdated = true;
	}
	
	//mutator for angle
	public void setAngle (float degrees)
	{
		angle = degrees;
		endAngle = degrees;
		Game.worldOutdated = true;
	}
	
	//mutator for scale
	public void setScale (float x, float y)
	{
		xScl = x;
		yScl = y;
		endXScl = x;
		endYScl = y;
		Game.worldOutdated = true;
	}
	
	/*************************
	 * Interpolation Methods *
	 *************************/
	
	public void moveInterpolate ()
	{	
		//if the object needs to be interpolated
		if (isInterpTrans)
		{		
			//increments movement
			float interval = (float)(Stopwatch.elapsedTimeInMilliseconds() - interpMoveTimeMs);
			setXPos(getXPos() + (moveSpeed / 1000 * interval * moveX));
			setYPos(getYPos() + (moveSpeed / 1000 * interval * moveY));
			
			//error check
			if (xPos <= endX + (moveSpeed / 2000 * interval * moveX) && xPos >= endX - (moveSpeed / 2000 * interval * moveX) || yPos <= endY + (moveSpeed / 2000 * interval * moveY) && yPos >= endY - (moveSpeed / 2000 * interval * moveY))
			{
				xPos = endX;
				yPos = endY;
				isInterpTrans = false;
				isRendered = true;
			}
			interpMoveTimeMs = Stopwatch.elapsedTimeInMilliseconds();
			Game.worldOutdated = true;
		}
	}
	
	public void rotateInterpolate ()
	{
		if (isInterpRot)
		{
			float increment = (float)(Stopwatch.elapsedTimeInMilliseconds() - interpRotTimeMs);
			
			if (angle <= endAngle + (rotSpeed / 1000 * increment) && angle >= endAngle - (rotSpeed / 1000 * increment))
			{
				angle = endAngle;
				isInterpRot = false;
			}
			
			if (isRotatingCCW)
			{
				//clamp the angle 0-360
				if (angle >= 360.0f)
					angle -= 360.0f * ((int)angle/360);
				else if (angle < 0.0f)
					angle = 0.0f;
				
				angle += rotSpeed / 1000 * increment;
			}
			else
			{
				if (angle <= 0.0f)
					angle = angle + 360.0f;
				else if (angle > 360.0f)
					angle = 360.0f;
				
				angle -= rotSpeed / 1000 * increment;
			}
			System.out.println(increment + " " + angle);
			//error check			
			interpRotTimeMs = Stopwatch.elapsedTimeInMilliseconds();
			
			Game.worldOutdated = true;
		}
	}
	
	public void scaleInterpolate ()
	{
		if (isInterpScl)
		{				
			float interval = (float)(Stopwatch.elapsedTimeInMilliseconds() - interpSclTimeMs);
			
			xScl += sclSpeed / 1000 * interval * scaleX;
			yScl += sclSpeed / 1000 * interval * scaleY;
			
			//error check
			if (xScl <= endXScl + (sclSpeed / 2000 * interval * scaleX) && xScl >= endXScl - (sclSpeed /2000 * interval * scaleX) || yScl <= endYScl + (sclSpeed / 2000 * interval * scaleY) && yScl >= endYScl - (sclSpeed / 2000 * interval * scaleY))
			{
				xScl = endXScl;
				isInterpScl = false;
			}
			
			interpSclTimeMs = Stopwatch.elapsedTimeInMilliseconds();
			
			Game.worldOutdated = true;
		}
	}
}
