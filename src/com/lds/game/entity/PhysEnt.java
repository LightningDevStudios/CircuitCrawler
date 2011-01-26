package com.lds.game.entity;

import java.util.ArrayList;

import com.lds.Enums.RenderMode;
import com.lds.game.Game;
import com.lds.Stopwatch;
import com.lds.Vector2f;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//interpolation data
	public float interpAngle, endAngle;
	public int moveTimeMs, rotTimeMs, sclTimeMs;
	protected float moveSpeed, rotSpeed, sclSpeed;
	public boolean isMoving, isRotating, isScaling, isRotatingCCW;
	protected Vector2f moveVec, moveInterpVec, endPosVec;
	protected Vector2f sclVec, sclInterpVec, endScaleVec;
	protected int moveInterpCount, sclInterpCount;
	//protected ArrayList<Vector2f> bounceList;
	protected Vector2f bounceVec;
	
	
	public PhysEnt(float size, float xPos, float yPos, boolean circular, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular, renderMode, moveSpeed, rotSpeed, sclSpeed);
	}
	
	public PhysEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, RenderMode renderMode, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, renderMode);
		
		//initialize interpolation variables
		endAngle = angle;
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
		this.sclSpeed = sclSpeed;
		moveVec = new Vector2f();
		sclVec = new Vector2f();
		moveInterpVec = new Vector2f();
		moveInterpCount = 0;
		sclInterpCount = 0;
		//bounceList = new ArrayList<Vector2f>();
		bounceVec = new Vector2f();
	}
	
	@Override
	public void update()
	{
		super.update();
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
		moveVec.set(x - getXPos(), y - getYPos());
		isMoving = true;
		moveTimeMs = Stopwatch.elapsedTimeMs();
		endPosVec = new Vector2f(x, y);
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
		sclVec.set(x - getXScl(), y - getYScl());
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
		endScaleVec = new Vector2f(x, y);
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		moveVec.set(x, y);
		isMoving = true;
		moveTimeMs = Stopwatch.elapsedTimeMs();
		endPosVec = Vector2f.add(moveVec, posVec);
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
		sclVec.set((x - 1) * getXScl(), (y - 1) * getYScl());
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
		endScaleVec = Vector2f.add(sclVec, scaleVec);
	}
	
	public void stop ()
	{
			
		if (isMoving)
		{
			posVec.sub(moveInterpVec);
			isMoving = false;
		}
		
		if (isRotating)
		{
			angle -= rotSpeed / 1000 * (float)(Stopwatch.elapsedTimeMs() - rotTimeMs);
			isRotating = false;
		}
		
		if (isScaling)
		{
			sclVec.sub(sclInterpVec);
			isScaling = false;
		}
	}
	
	/*********************
	 * Collision Methods *
	 *********************/
	
	//TODO: Rectangle vs. Circle, complicated physics, double collision, corner collision
	
	@Override
	protected boolean isCircleCollidingWithCircle (Entity ent)
	{
		boolean output = super.isCircleCollidingWithCircle(ent);
		if (output)
		{
			this.circleWithCircleBounce(ent);
			ent.circleWithCircleBounce(ent);
		}
		return output;
	}
	
	@Override
	protected boolean isRectangleCollidingWithCircle (Entity ent) 
	{
		boolean output = super.isRectangleCollidingWithCircle(ent);
		return output;
	}
	
	@Override
	protected boolean isRectangleCollidingWithRectangle (Entity ent)
	{
		boolean output = super.isRectangleCollidingWithRectangle(ent);
		if (output)
		{
			this.rectangleWithRectangleBounce(ent);
			ent.rectangleWithRectangleBounce(this);
		}
		return output;
	}
	
	@Override
	public void circleWithCircleBounce (Entity ent)
	{
		
	}
	
	@Override
	public void rectangleWithCircleBounce (Entity ent)
	{
		
	}
	
	@Override
	public void rectangleWithRectangleBounce (Entity ent)
	{
		//gets an array of all the vectors between this and the ent's vertices
		Vector2f[] vertDistVecs = new Vector2f[4];
		for (int i = 0; i < 4; i++)
		{
			vertDistVecs[i] = Vector2f.sub(ent.vertVecs[i], this.posVec);
		}
		//goes through the vectors and sorts them from low to high (thanks Mr. Carlson)
		int i, k, maxPos;
		Vector2f temp = new Vector2f();
	    for (k = vertDistVecs.length; k >= 2; k--)
	    {
	    	maxPos = 0; 
	        for (i = 1; i < k; i++) 
	        {
	             if (vertDistVecs[i].mag() > vertDistVecs[maxPos].mag()) 
	                  maxPos = i; 
	        }
	        temp.set(vertDistVecs[maxPos]); 
	        vertDistVecs[maxPos].set(vertDistVecs[k-1]); 
	        vertDistVecs[k-1].set(temp); 
	    }
	    //calculate the bouceVec
		Vector2f bounceSide = Vector2f.normalize(Vector2f.sub(vertDistVecs[0], vertDistVecs[1]));
		bounceSide.scale(this.moveInterpVec.dot(bounceSide));
		this.setBounceVec(Vector2f.sub(bounceSide, this.moveInterpVec));
	}
	
	/**********************************
	 * Instant Transformation Methods *
	 **********************************/
	
	//mutators for position
	public void setPos (Vector2f v)
	{
		moveInterpVec = Vector2f.sub(v, posVec);
		posVec.set(v);
		Game.worldOutdated = true;
	}
	
	public void setPos (float x, float y)
	{
		setPos(new Vector2f(x, y));
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
		sclVec.set(x, y);
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
				moveInterpVec = Vector2f.scale(Vector2f.normalize(moveVec), moveSpeed / 1000 * (Stopwatch.elapsedTimeMs() - moveTimeMs));
				
				if (moveVec.mag() - moveInterpVec.mag() * moveInterpCount <= moveInterpVec.mag())
				{
					posVec = endPosVec;
					isMoving = false;
					moveInterpCount = 0;
				}
				else
				{
					posVec.add(moveInterpVec);
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
			if(sclSpeed <= 0)
			{
				sclSpeed = 0;
				isScaling = false;
				sclInterpCount = 0;
			}
			else 
			{
				sclInterpCount++;
				sclInterpVec = Vector2f.scale(Vector2f.normalize(sclVec), sclSpeed / 1000 * (Stopwatch.elapsedTimeMs() - sclTimeMs));
				
				if (sclVec.mag() - sclInterpVec.mag() * sclInterpCount <= sclInterpVec.mag())
				{
					scaleVec = endScaleVec;
					isScaling = false;
					sclInterpCount = 0;
				}
				else
				{
					scaleVec.add(sclInterpVec);
				}
				
				Game.worldOutdated = true;
			}
							
			sclTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	public Vector2f getBounceVec ()
	{
		/* TODO: make this calculate with multiple collisions
	 	if (bounceList.isEmpty())
		{
			System.out.println("Sorry, no bounceVecs found in bounceList");
			return null;
		}
		else
		{
			Vector2f bounceVec = new Vector2f(bounceList.get(0));
			for (int i = 1; i < bounceList.size(); i++)
			{
				bounceVec.add(bounceList.get(i));
			}
			return bounceVec;
		}*/
		return bounceVec;
	}
	
	public void setBounceVec (Vector2f v)
	{
		bounceVec = v;
	}
	
	public Vector2f getMoveInterpVec ()
	{
		return moveInterpVec;
	}
	
	public void setMoveInterpVec (Vector2f v)
	{
		moveInterpVec = v;
	}
	
}
