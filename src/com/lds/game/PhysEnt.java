package com.lds.game;

import com.lds.Enums.RenderMode;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//constants
	public static final float DEFAULT_SPEED = 0.5f;
	
	//interpolation data
	public float interpX, interpY, interpXScl, interpYScl, interpAngle, endX, endY, endXScl, endYScl, endAngle, speed;
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
		speed = _speed;
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
		endX = x;
		endY = y;
		isInterpTrans = true;
	}
	
	//sets angle of an entity to a new value
	public void rotateTo (float degrees)
	{
		endAngle = degrees;
		interpAngle = degrees - angle;
		isInterpRot = true;
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
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		interpX = x;
		interpY = y;
		endX = xPos + x;
		endY = yPos + y;
		isInterpTrans = true;
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (float degrees)
	{
		endAngle = angle + degrees;
		interpAngle = degrees;	
		isInterpRot = true;
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
	}
	
	public void stop ()
	{
		if (isInterpTrans)
		{
			xPos -= (speed / 10) * interpX;
			yPos -= (speed / 10) * interpY;
			endX = xPos;
			endY = yPos;
			isInterpTrans = false;
		}
		
		if (isInterpRot)
		{
			angle -= (speed / 10) * interpAngle;
			endAngle = angle;
			isInterpRot = false;
		}
		
		if (isInterpScl)
		{
			xScl -= (speed / 10) * interpXScl;
			yScl -= (speed / 10) * interpYScl;
			endXScl = xScl;
			endYScl = yScl;
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
			xPos += (speed / 10) * interpX;
			yPos += (speed / 10) * interpY;
			
			//error check
			if (xPos <= endX + (speed / 2) && xPos >= endX - (speed / 2))
			{
				xPos = endX;
				isInterpTrans = false;
			}
			/*if (yPos <= endY + speed / 2 && yPos >= endY - speed / 2)
			{
				yPos = endY;
			}*/
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
			//increments angle
			//TODO Find correlation between modification of speed and accuracy of end check.
			angle += speed * interpAngle / 10;
			//error check
			if (angle <= endAngle + (speed / 2) && angle >= endAngle - (speed / 2))
			{
				angle = endAngle;
				isInterpRot = false;
			}
			
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
			if (xScl <= endXScl + (speed / 2) && xScl >= endXScl - (speed / 2))
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