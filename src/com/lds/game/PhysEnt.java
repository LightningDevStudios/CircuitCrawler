package com.lds.game;

import com.lds.Enums.RenderMode;
import com.lds.Point;
import com.lds.Stopwatch;
import com.lds.Vector2f;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//interpolation data
	public float interpAngle, endAngle;
	public int moveTimeMs, rotTimeMs, sclTimeMs;
	protected float moveSpeed, rotSpeed, sclSpeed;
	private boolean isMoving, isRotating, isScaling, isRotatingCCW;
	protected Vector2f moveVec, moveInterpVec;
	protected Vector2f sclVec, sclInterpVec;
	protected float startMoveX, startMoveY, startScaleX, startScaleY;
	protected int moveInterpCount, sclInterpCount;
	
	public PhysEnt(float size, float xPos, float yPos, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, renderMode, moveSpeed, rotSpeed, sclSpeed);
	}
	
	public PhysEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, renderMode);
		
		//initialize interpolation variables
		endAngle = angle;
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
		this.sclSpeed = sclSpeed;
		moveVec = new Vector2f();
		sclVec = new Vector2f();
		moveInterpCount = 0;
		sclInterpCount = 0;
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
		moveVec.set(x - xPos, y - yPos);
		isMoving = true;
		moveTimeMs = Stopwatch.elapsedTimeMs();
		startMoveX = xPos;
		startMoveY = yPos;
	}
	
	//sets angle of an entity to a new value
	public void rotateTo (float degrees)
	{
		if (!isRotating)
		{
		endAngle = degrees;
		float dist = degrees - angle;
		
		if (dist >= 180 || dist >= -180 && dist <= 0)
			isRotatingCCW = false;
		else
			isRotatingCCW = true;
		
		if (dist != 0)
			isRotating = true;
		
		rotTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (float x, float y)
	{
		sclVec.set(x - xScl, y - yScl);
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		moveVec.set(x, y);
		isMoving = true;
		moveTimeMs = Stopwatch.elapsedTimeMs();
		startMoveX = xPos;
		startMoveY = yPos;
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (float degrees)
	{
		endAngle = angle + degrees;
		interpAngle = degrees;	
		isRotating = true;
		rotTimeMs = Stopwatch.elapsedTimeMs();
	}
	
	//scales relative to current scaling
	//i.e. if ent1 is scaled (2.0f, 2.0f), if you do ent1.scale(3.0f, 3.0f) the final scaling will be (6.0f, 6.0f)
	public void scale (float x, float y)
	{
		sclVec.set((x - 1) * xScl, (y - 1) * yScl);
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
	}
	
	public void stop ()
	{
		if (isMoving)
		{
			xPos -= moveInterpVec.getX();
			yPos -= moveInterpVec.getY();
			isMoving = false;
		}
		
		if (isRotating)
		{
			angle -= rotSpeed / 1000 * (float)(Stopwatch.elapsedTimeMs() - rotTimeMs);
			isRotating = false;
		}
		
		if (isScaling)
		{
			xScl -= sclInterpVec.getX();
			yScl -= sclInterpVec.getY();
			isScaling = false;
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
		Game.worldOutdated = true;
	}
	
	/*************************
	 * Interpolation Methods *
	 *************************/
	
	public void moveInterpolate ()
	{	
		//if the object needs to be interpolated
		if (isMoving)
		{
			if(moveSpeed <= 0)
			{
				moveSpeed = 0;
				isMoving = false;
				moveInterpCount = 0;
			}
			else 
			{
				moveInterpCount++;
				moveInterpVec = Vector2f.scale(Vector2f.norm(moveVec), moveSpeed / 1000 * (Stopwatch.elapsedTimeMs() - moveTimeMs));
				
				if (moveVec.mag() - moveInterpVec.mag() * moveInterpCount <= moveInterpVec.mag())
				{
					//TODO this doesn't take into account differences in framerate. Hence the inaccuracies
					//Vector2f vecToEnd = Vector2f.sub(moveVec, Vector2f.scale(moveInterpVec, moveInterpCount));
					xPos = startMoveX + moveVec.getX();
					yPos = startMoveY + moveVec.getY();
					isMoving = false;
					moveInterpCount = 0;
				}
				else
				{
					xPos += moveInterpVec.getX();
					yPos += moveInterpVec.getY();
				}
				
				Game.worldOutdated = true;
			}
							
			moveTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	public void rotateInterpolate ()
	{
		if (isRotating)
		{
			float increment = (float)(Stopwatch.elapsedTimeMs() - rotTimeMs);
			
			if (angle <= endAngle + (rotSpeed / 1000 * increment) && angle >= endAngle - (rotSpeed / 1000 * increment))
			{
				angle = endAngle;
				isRotating = false;
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
			rotTimeMs = Stopwatch.elapsedTimeMs();
			
			Game.worldOutdated = true;
		}
	}
	
	public void scaleInterpolate ()
	{
		if (isScaling)
		{				
			sclInterpCount++;
			sclInterpVec = Vector2f.scale(Vector2f.norm(sclVec), sclSpeed / 1000 * (Stopwatch.elapsedTimeMs() - sclTimeMs));
			xScl += sclInterpVec.getX();
			yScl += sclInterpVec.getY();
			
			if (sclVec.mag() - sclInterpVec.mag() * sclInterpCount <= 0)
			{
				Vector2f vecToEnd = Vector2f.sub(sclVec, Vector2f.scale(sclInterpVec, sclInterpCount));
				xScl += vecToEnd.getX();
				yScl += vecToEnd.getY();
				isScaling = false;
				isRendered = true;
				sclInterpCount = 0;
			}
			
			sclTimeMs = Stopwatch.elapsedTimeMs();
			Game.worldOutdated = true;
		}
	}
}
